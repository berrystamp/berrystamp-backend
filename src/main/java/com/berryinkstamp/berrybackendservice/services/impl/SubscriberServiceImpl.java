package com.berryinkstamp.berrybackendservice.services.impl;

import com.berryinkstamp.berrybackendservice.dtos.request.AddSubscriberRequest;
import com.berryinkstamp.berrybackendservice.models.Subscriber;
import com.berryinkstamp.berrybackendservice.repositories.SubscriberRepository;
import com.berryinkstamp.berrybackendservice.services.SubscriberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {

    private SubscriberRepository subscriberRepository;
    @Override
    public Subscriber addSubscriber(AddSubscriberRequest addSubscriberRequest) {
        Subscriber subscriber = Subscriber.builder().
                username(addSubscriberRequest.getUsername()).
                phoneNumber(addSubscriberRequest.getPhoneNumber()).
                email(addSubscriberRequest.getEmail()).build();

        subscriberRepository.save(subscriber);
        return subscriber;
    }

}
