package br.com.infrastructure.repository;

import br.com.infrastructure.entity.SubjectEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SubjectRepository implements PanacheRepository<SubjectEntity> {
    
    public Optional<SubjectEntity> findByCode(String code) {
        return find("code", code).firstResultOptional();
    }
}
