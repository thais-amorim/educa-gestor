package br.com.domain.exception;

public class CourseNotFoundException extends NotFoundException {

    public CourseNotFoundException(Long courseId) {
        super("Course not found with ID: " + courseId);
    }
}
