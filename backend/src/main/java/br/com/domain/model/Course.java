package br.com.domain.model;

import br.com.infrastructure.entity.CourseEntity;

import java.util.List;

public class Course {
    private Long id;
    private String code;
    private String name;
    private List<Semester> semesters;

    public Course() {}

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Course(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Course(Long id, String code, String name, List<Semester> semesters) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.semesters = semesters;
    }

    public CourseEntity toEntity() {
        return new CourseEntity(this.code, this.name);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<Semester> semesters) {
        this.semesters = semesters;
    }
}
