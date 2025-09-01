package br.com.infrastructure.repository;

import br.com.infrastructure.entity.SemesterSubjectEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SemesterSubjectRepository implements PanacheRepository<SemesterSubjectEntity> {
    
    public List<SemesterSubjectEntity> findBySemesterId(Long semesterId) {
        return find("semester.id", semesterId).list();
    }
    
    public void deleteBySemesterId(Long semesterId) {
        delete("semester.id", semesterId);
    }
}
