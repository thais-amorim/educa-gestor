package br.com.infrastructure.entity;

import br.com.domain.model.Subject;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subject")
public class SubjectEntity extends PanacheEntityBase {
    @Id
    private String code;
    private String name;
    private String instructorName;
    private Long workload;

    public SubjectEntity() {}

    public SubjectEntity(String code, String name, String instructorName, Long workload) {
        this.code = code;
        this.name = name;
        this.instructorName = instructorName;
        this.workload = workload;
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

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Long getWorkload() {
        return workload;
    }

    public void setWorkload(Long workload) {
        this.workload = workload;
    }

    public Subject toDomain() {
        return new Subject(
                this.code,
                this.name,
                this.instructorName,
                this.workload
        );
    }
}
