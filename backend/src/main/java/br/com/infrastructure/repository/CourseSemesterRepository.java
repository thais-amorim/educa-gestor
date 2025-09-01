package br.com.infrastructure.repository;

import br.com.infrastructure.entity.CourseSemesterEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CourseSemesterRepository implements PanacheRepository<CourseSemesterEntity> {
    
    public List<CourseSemesterEntity> findByCourseId(Long courseId) {
        return find("course.id", courseId).list();
    }
    
    public void deleteByCourseId(Long courseId) {
        delete("course.id", courseId);
    }
}
