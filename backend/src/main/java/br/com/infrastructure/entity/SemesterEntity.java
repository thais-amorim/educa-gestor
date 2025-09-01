package br.com.infrastructure.entity;

import br.com.domain.model.Semester;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "semester")
public class SemesterEntity extends PanacheEntity {
    private String code;

    public SemesterEntity() {}

    public SemesterEntity(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Semester toDomain() {
        return new Semester(this.id, this.code);
    }
}
