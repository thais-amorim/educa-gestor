package br.com.domain.exception;

public class SubjectNotFoundException extends NotFoundException {
    public SubjectNotFoundException(String code) {
        super("Subject not found for code: " + code);
    }
}
