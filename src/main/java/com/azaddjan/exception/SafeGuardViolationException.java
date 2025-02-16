package com.azaddjan.exception;

public class SafeGuardViolationException extends RuntimeException {
    public SafeGuardViolationException(String message) {
        super("Your question was blocked due to content restrictions: " + message);
    }
}

