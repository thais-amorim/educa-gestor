package br.com.infrastructure.entity;

import br.com.domain.model.Subject;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "subject")
public class SubjectEntity extends PanacheEntity {
    private String code;
    private String name;
    private Long instructorName;
    private Long workload;
    private Date beginAt;
    private Date endAt;

    public SubjectEntity() {}

    public SubjectEntity(String code, String name, Long instructorName, Long workload, Date beginAt, Date endAt) {
        this.code = code;
        this.name = name;
        this.instructorName = instructorName;
        this.workload = workload;
        this.beginAt = beginAt;
        this.endAt = endAt;
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

    public Date getBeginAt() {
        return beginAt;
    }

    public void setBeginAt(Date beginAt) {
        this.beginAt = beginAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Subject toDomain() {
        return new Subject(
                this.id,
                this.code,
                this.name,
                this.instructorName,
                this.workload,
                this.beginAt,
                this.endAt
        );
    }
}
