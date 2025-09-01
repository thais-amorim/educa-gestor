package br.com.domain.model;

import br.com.infrastructure.entity.SemesterEntity;

import java.util.List;

public class Semester {
    private Long id;
    private String code;
    private List<Subject> subjects;

    public Semester() {}

    public Semester(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public Semester(Long id, String code, List<Subject> subjects) {
        this.id = id;
        this.code = code;
        this.subjects = subjects;
    }

    public SemesterEntity toEntity() {
        return new SemesterEntity(this.code);
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

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
