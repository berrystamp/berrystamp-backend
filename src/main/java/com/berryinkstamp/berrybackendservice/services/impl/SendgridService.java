package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.dtos.request.EmailRequest;
import com.berryinkstamp.berrybackendservice.enums.MailProvider;
import com.berryinkstamp.berrybackendservice.services.MailProviderService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SendgridService implements MailProviderService {

    @Value("${sendgrid.api.key}")
    private String SENDGRID_API_KEY;

    @Value("${mail.from.email}")
    private String MAIL_FROM_EMAIL;

    @Value("${mail.from.name}")
    private String MAIL_FROM_NAME;

    @Override
    public void send(EmailRequest emailRequest) {
        Email from = new Email(MAIL_FROM_EMAIL, MAIL_FROM_NAME);
        Mail mail = new Mail();
        mail.setSubject(emailRequest.getSubject());
        mail.setFrom(from);
        mail.setTemplateId(emailRequest.getTemplateId());

        Personalization personalization = new Personalization();
        if(emailRequest.getRecipients().contains(";")){
            for(String recipient : emailRequest.getRecipients().split(";")){
                personalization.addTo(new Email(recipient));
            }
        }else{
            personalization.addTo(new Email(emailRequest.getRecipients()));
        }

        if (emailRequest.getTemplateId() != null) {
            mail.setTemplateId(emailRequest.getTemplateId());
            if (!emailRequest.getPlaceholders().isEmpty()) {
                addPlaceholders(personalization, emailRequest.getPlaceholders());
            }
        } else {
            Content content = new Content("text/html", emailRequest.getBody());
            mail.addContent(content);
        }

        mail.addPersonalization(personalization);
        mail.setSubject(emailRequest.getSubject());
        processAttachment(mail, emailRequest.getFiles());

        send(mail);
    }

    @Override
    public boolean canApply(MailProvider provider) {
        return MailProvider.SENDGRID == provider;
    }

    @SneakyThrows
    private void processAttachment(Mail mail, List<File> attachments) {
        if (attachments == null) {
            return;
        }
        for (File attachment: attachments) {
            String pathInTemporaryDirectory = attachment.getPath();
            addAttachment(mail, pathInTemporaryDirectory);
        }

    }

    private void addAttachment(Mail mail, String filePath) throws IOException {
        try (final InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
            final Attachments attachments = new Attachments
                    .Builder(fileName, inputStream)
                    .withType("application/pdf")
                    .build();
            mail.addAttachments(attachments);
        }
    }

    private void addPlaceholders(Personalization personalization, Map<String, String> placeholders) {
        placeholders.forEach(personalization::addDynamicTemplateData);
    }

    private void send(Mail mail) {
        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
            log.info("email sent via sendgrid");
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("error sending  mail: ", ex);
        }
    }
}
