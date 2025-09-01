package br.com.infrastructure.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "semester_subject")
public class SemesterSubjectEntity extends PanacheEntity {
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject;
    
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private SemesterEntity semester;

    public SemesterSubjectEntity() {}

    public SemesterSubjectEntity(SubjectEntity subject, SemesterEntity semester) {
        this.subject = subject;
        this.semester = semester;
    }

    public SubjectEntity getSubject() {
        return subject;
    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
    }

    public SemesterEntity getSemester() {
        return semester;
    }

    public void setSemester(SemesterEntity semester) {
        this.semester = semester;
    }
}
