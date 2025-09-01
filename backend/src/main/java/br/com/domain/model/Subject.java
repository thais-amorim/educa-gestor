package br.com.domain.model;

import br.com.infrastructure.entity.SubjectEntity;

public class Subject {
    private String code;
    private String name;
    private Long instructorName;
    private Long workload;

    public Subject(String code, String name, Long instructorName, Long workload) {
        this.code = code;
        this.name = name;
        this.instructorName = instructorName;
        this.workload = workload;
    }

    public SubjectEntity toEntity() {
        return new SubjectEntity(
            this.code,
            this.name,
            this.instructorName,
            this.workload
        );
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

    public Long getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(Long instructorName) {
        this.instructorName = instructorName;
    }

    public Long getWorkload() {
        return workload;
    }

    public void setWorkload(Long workload) {
        this.workload = workload;
    }
}
