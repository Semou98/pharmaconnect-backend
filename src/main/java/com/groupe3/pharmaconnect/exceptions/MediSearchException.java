package com.groupe3.pharmaconnect.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class MediSearchException extends RuntimeException {
    private final HttpStatus status;

    public MediSearchException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
