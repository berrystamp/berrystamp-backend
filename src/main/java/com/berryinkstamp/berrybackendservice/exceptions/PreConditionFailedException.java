package com.berryinkstamp.berrybackendservice.exceptions;

public class PreConditionFailedException extends RuntimeException {

    public PreConditionFailedException(String message) {
        super(message);
    }
}
