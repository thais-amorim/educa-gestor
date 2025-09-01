package br.com.domain.service;

import br.com.domain.model.Course;

import java.util.List;

public interface CourseService {
    public Course create(Course course);
    public Course update(Course course);
    public Boolean delete(Long courseId);
    public Course findById(Long courseId);
    public List<Course> findAll();
    public Course addSemesterToCourse(Long courseId, Long semesterId);
    public Course removeSemesterFromCourse(Long courseId, Long semesterId);
}
