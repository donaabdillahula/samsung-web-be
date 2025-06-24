package com.dona_samsung_web_project.samsung_web_be.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Unauthorized");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
