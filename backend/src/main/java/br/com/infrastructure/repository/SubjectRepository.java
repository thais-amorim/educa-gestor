package br.com.infrastructure.repository;

import br.com.infrastructure.entity.SubjectEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SubjectRepository implements PanacheRepository<SubjectEntity> {
}
