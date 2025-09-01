package br.com.infrastructure.repository;

import br.com.infrastructure.entity.SemesterEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SemesterRepository implements PanacheRepository<SemesterEntity> {
}
