package br.com.domain.exception;

import java.io.Serializable;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
