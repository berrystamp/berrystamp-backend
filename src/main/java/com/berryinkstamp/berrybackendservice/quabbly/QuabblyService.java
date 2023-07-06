
package com.berryinkstamp.berrybackendservice.quabbly;

import com.berryinkstamp.berrybackendservice.models.User;
import com.berryinkstamp.berrybackendservice.quabbly.dto.Metadata;
import com.berryinkstamp.berrybackendservice.quabbly.dto.QuabblyRecordInputDTO;
import com.berryinkstamp.berrybackendservice.quabbly.dto.Record;
import com.berryinkstamp.berrybackendservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuabblyService {

    private final QuabblyRestUtil restUtil;

    private final UserRepository userRepository;

    @Value("${quabbly.user.board.id}")
    private String userInfoBoardId;

    @Value("${quabbly.user.field.email.id}")
    private String emailFieldForUserBoard;

    @Value("${quabbly.user.field.token.id}")
    private String tokenFieldForUserBoard;

    @Value("${quabbly.user.field.name.id}")
    private String nameFieldForUserBoard;

    @Value("${quabbly.user.field.emailType.id}")
    private String emailTypeFieldForUserBoard;



       public void sendActivationEmail(User user, String token) {

           try {
               QuabblyRecordInputDTO quabblyRecordInputDTO = new QuabblyRecordInputDTO();
               Set<Metadata> metadata = new HashSet<>();
               metadata.add(getEmailMetadata(user.getEmail()));
               metadata.add(getFullNameMetadata(user.getName()));
               metadata.add(getTokenMetadata(token));
               metadata.add(getEmailTypeMetadata("activate"));

               quabblyRecordInputDTO.setMetadata(metadata);
               Record record = restUtil.createRecord(quabblyRecordInputDTO, userInfoBoardId);
               if (record != null ) {
                   user.setSpreadsheetId(record.getId());
                   userRepository.save(user);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }

    }



    public void resendToken(User user, String token) {
           try {

               QuabblyRecordInputDTO recordInputDTO = new QuabblyRecordInputDTO();
               Set<Metadata> metadata = new HashSet<>();
               metadata.add(getTokenMetadata(token));
               metadata.add(getEmailTypeMetadata("resend"));
               recordInputDTO.setMetadata(metadata);
               updateRecord(recordInputDTO, user.getSpreadsheetId());
           } catch (Exception e) {
               e.printStackTrace();
           }


    }

    public void forgetPasswordToken(User user, String token) {
           try {
               QuabblyRecordInputDTO recordInputDTO = new QuabblyRecordInputDTO();
               Set<Metadata> metadata = new HashSet<>();
               metadata.add(getTokenMetadata(token));
               metadata.add(getEmailTypeMetadata("forget password"));
               recordInputDTO.setMetadata(metadata);
               updateRecord(recordInputDTO, user.getSpreadsheetId());
           }catch (Exception e) {
               e.printStackTrace();
           }


    }

    public void welcomeEmail(User user) {
           try {
               QuabblyRecordInputDTO recordInputDTO = new QuabblyRecordInputDTO();
               Set<Metadata> metadata = new HashSet<>();
               metadata.add(getEmailTypeMetadata("welcome"));
               recordInputDTO.setMetadata(metadata);
               updateRecord(recordInputDTO, user.getSpreadsheetId());
           } catch (Exception exception) {
               exception.printStackTrace();
           }


    }

    private void updateRecord(QuabblyRecordInputDTO recordInputDTO, String quabblyRecordId) throws IOException {
        Record record = restUtil.updateRecord(recordInputDTO, userInfoBoardId, quabblyRecordId);
        if (record == null ) {
            throw new RuntimeException("an error occurred");
        }
    }



    private Metadata getEmailTypeMetadata(String type) {
        return new Metadata(type, emailTypeFieldForUserBoard);
    }

    private Metadata getEmailMetadata(String email) {
        return new Metadata(email, emailFieldForUserBoard);
    }

    private Metadata getFullNameMetadata(String fullName) {
        return new Metadata(fullName, nameFieldForUserBoard);
    }

    private Metadata getTokenMetadata(String token) {
        return new Metadata(token, tokenFieldForUserBoard);
    }

    public void updateMetadata(Set<Metadata> metadataList, Object value, String id) {
        if (Objects.nonNull(value))  {
            metadataList.add(new Metadata(value, id));
        }
    }
}