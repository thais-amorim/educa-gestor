package br.com.domain.exception;

public class SubjectAlreadyExistsException extends AlreadyExistsException {
    public SubjectAlreadyExistsException(String code) {
        super("Subject already exists for code: " + code);
    }
}
