package br.com.infrastructure.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "course_semester")
public class CourseSemesterEntity extends PanacheEntity {
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;
    
    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private SemesterEntity semester;

    public CourseSemesterEntity() {}

    public CourseSemesterEntity(CourseEntity course, SemesterEntity semester) {
        this.course = course;
        this.semester = semester;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public SemesterEntity getSemester() {
        return semester;
    }

    public void setSemester(SemesterEntity semester) {
        this.semester = semester;
    }
}
