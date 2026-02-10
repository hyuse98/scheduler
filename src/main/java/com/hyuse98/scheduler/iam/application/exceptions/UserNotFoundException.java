package com.hyuse98.scheduler.iam.application.exceptions;

import com.hyuse98.scheduler.iam.domain.exceptions.MessageException;

public class UserNotFoundException extends MessageException {
    public UserNotFoundException(String message) {
        super("User not Found");
    }
}