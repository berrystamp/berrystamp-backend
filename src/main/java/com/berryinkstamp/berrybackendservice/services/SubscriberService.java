package com.berryinkstamp.berrybackendservice.services;

import com.berryinkstamp.berrybackendservice.dtos.request.AddSubscriberRequest;
import com.berryinkstamp.berrybackendservice.models.Subscriber;
import org.springframework.stereotype.Service;

@Service
public interface SubscriberService {

  Subscriber addSubscriber(AddSubscriberRequest addSubscriberRequest);
}
