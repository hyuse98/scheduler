package com.hyuse98.scheduler.core.application.exceptions;

public class ServiceProviderAlreadyExistException extends RuntimeException {
    public ServiceProviderAlreadyExistException(String message) {
        super(message);
    }
}
