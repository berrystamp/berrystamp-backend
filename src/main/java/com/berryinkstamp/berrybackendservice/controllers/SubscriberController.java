package com.berryinkstamp.berrybackendservice.controllers;

import com.berryinkstamp.berrybackendservice.dtos.request.AddSubscriberRequest;
import com.berryinkstamp.berrybackendservice.services.SubscriberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/controller")
public class SubscriberController {

    private SubscriberService subscriberService;

    @PostMapping("/newSubscriber")
    public ResponseEntity<?> addSubscriber(@RequestBody AddSubscriberRequest addSubscriberRequest){
        return new ResponseEntity<>(subscriberService.addSubscriber(addSubscriberRequest), HttpStatus.CREATED);
    }
}
