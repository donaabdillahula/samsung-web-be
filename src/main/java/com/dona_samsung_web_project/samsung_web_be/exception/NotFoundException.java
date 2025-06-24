package com.dona_samsung_web_project.samsung_web_be.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Data is not found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
