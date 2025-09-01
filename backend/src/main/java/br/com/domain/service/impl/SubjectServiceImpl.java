package br.com.domain.service.impl;

import br.com.domain.exception.SubjectAlreadyExistsException;
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
        Optional<SubjectEntity> existingSubject = subjectRepository.findByIdOptional(subject.getCode());
        if (existingSubject.isPresent()) {
            throw new SubjectAlreadyExistsException(subject.getCode());
        }

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
    public Subject update(Subject subject) {
        Optional<SubjectEntity> optional = subjectRepository.findByIdOptional(subject.getCode());
        SubjectEntity existingSubject = optional.orElseThrow(() -> new SubjectNotFoundException(subject.getCode()));
        
        existingSubject.setName(subject.getName());
        existingSubject.setInstructorName(subject.getInstructorName());
        existingSubject.setWorkload(subject.getWorkload());
        
        subjectRepository.persist(existingSubject);
        
        return existingSubject.toDomain();
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll().stream().map(SubjectEntity::toDomain).toList();
    }
}
