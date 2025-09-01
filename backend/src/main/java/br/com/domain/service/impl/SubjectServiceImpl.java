package br.com.domain.service.impl;

import br.com.domain.exception.SubjectNotFoundException;
import br.com.domain.model.Subject;
import br.com.domain.service.SubjectService;
import br.com.infrastructure.entity.SubjectEntity;
import br.com.infrastructure.repository.SubjectRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SubjectServiceImpl implements SubjectService {
    @Inject
    SubjectRepository subjectRepository;

    @Override
    @Transactional
    public Subject create(Subject subject) {
        SubjectEntity entity = subject.toEntity();
        subjectRepository.persist(entity);
        return subject;
    }

    @Override
    @Transactional
    public Boolean delete(String subjectCode) {
        return subjectRepository.deleteById(subjectCode);
    }

    @Override
    public Subject findByCode(String subjectCode) {
        Optional<SubjectEntity> optional = subjectRepository.findByIdOptional(subjectCode);
        SubjectEntity obtained = optional.orElseThrow(() -> new SubjectNotFoundException(subjectCode));
        return obtained.toDomain();
    }

    @Override
    @Transactional
    public Subject update(Subject subjectId) {
//        Optional<SubjectEntity> optional = subjectRepository.findByIdOptional(newSubject.getId());
//        SubjectEntity oldSubject = optional.orElseThrow(() -> new SubjectNotFoundException(newSubject.getId()));

        return null;
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll().stream().map(SubjectEntity::toDomain).toList();
    }
}
