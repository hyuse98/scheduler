package com.hyuse98.scheduler.iam.domain.exceptions;

public class UserNotFoundException extends MessageException{
    public UserNotFoundException(String message) {
        super("User not Found");
    }
}