package br.com.domain.exception;

public class SemesterNotFoundException extends RuntimeException {
    public SemesterNotFoundException(Long id) {
        super("Semester not found with id: " + id);
    }
}
