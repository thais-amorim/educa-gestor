package br.com.infrastructure.entity;

import br.com.domain.model.Course;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "course")
public class CourseEntity extends PanacheEntity {
    private String code;
    private String name;

    public CourseEntity() {}

    public CourseEntity(String code, String name) {
        this.code = code;
        this.name = name;
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

    public Course toDomain() {
        return new Course(this.code, this.name);
    }
}
