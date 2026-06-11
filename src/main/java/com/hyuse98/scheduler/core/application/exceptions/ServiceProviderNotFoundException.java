package com.hyuse98.scheduler.core.application.exceptions;

public class ServiceProviderNotFoundException extends RuntimeException {
    public ServiceProviderNotFoundException(String message) {
        super(message);
    }
}
