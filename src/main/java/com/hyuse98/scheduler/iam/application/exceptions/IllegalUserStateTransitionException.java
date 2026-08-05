package com.hyuse98.scheduler.iam.application.exceptions;

public class IllegalUserStateTransitionException extends RuntimeException {
    public IllegalUserStateTransitionException(String message) {
        super(message);
    }
}
