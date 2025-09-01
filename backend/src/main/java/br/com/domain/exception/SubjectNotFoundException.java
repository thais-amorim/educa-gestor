package br.com.domain.exception;

public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException(Long id) {
        super("Subject not found for id: " + id);
    }
    
    public SubjectNotFoundException(String code) {
        super("Subject not found for code: " + code);
    }
}
