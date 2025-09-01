package br.com.domain.exception;

public class CourseNotFoundException extends RuntimeException {
    
    public CourseNotFoundException(Long courseId) {
        super("Course not found with ID: " + courseId);
    }
    
    public CourseNotFoundException(String message) {
        super(message);
    }
    
    public CourseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
