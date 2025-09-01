package br.com.infrastructure.repository;

import br.com.infrastructure.entity.SubjectEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SubjectRepository implements PanacheRepositoryBase<SubjectEntity, String> {
    
    public Optional<SubjectEntity> findByCode(String code) {
        return findByIdOptional(code);
    }
}
