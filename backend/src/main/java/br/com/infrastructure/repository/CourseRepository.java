package br.com.infrastructure.repository;

import br.com.infrastructure.entity.CourseEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CourseRepository implements PanacheRepository<CourseEntity> {
    
    public Optional<CourseEntity> findByCode(String code) {
        return find("code", code).firstResultOptional();
    }
}
