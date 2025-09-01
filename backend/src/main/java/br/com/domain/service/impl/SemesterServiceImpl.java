package br.com.domain.service.impl;

import br.com.domain.exception.SemesterNotFoundException;
import br.com.domain.exception.SubjectNotFoundException;
import br.com.domain.model.Semester;
import br.com.domain.model.Subject;
import br.com.domain.service.SemesterService;
import br.com.infrastructure.entity.SemesterEntity;
import br.com.infrastructure.entity.SemesterSubjectEntity;
import br.com.infrastructure.entity.SubjectEntity;
import br.com.infrastructure.repository.SemesterRepository;
import br.com.infrastructure.repository.SemesterSubjectRepository;
import br.com.infrastructure.repository.SubjectRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SemesterServiceImpl implements SemesterService {
    
    @Inject
    SemesterRepository semesterRepository;
    
    @Inject
    SemesterSubjectRepository semesterSubjectRepository;
    
    @Inject
    SubjectRepository subjectRepository;

    @Override
    @Transactional
    public Semester create(Semester semester) {
        SemesterEntity entity = saveSemester(semester);
        saveRelatedSubjects(semester, entity);
        return semester;
    }

    private void saveRelatedSubjects(Semester semester, SemesterEntity semesterEntity) {
        if (semester.getSubjects() != null && !semester.getSubjects().isEmpty()) {
            for (Subject subject : semester.getSubjects()) {
                SubjectEntity subjectEntity = saveSingleSubject(subject);
                saveSemesterSubjectRelation(semesterEntity, subjectEntity);
            }
        }
    }

    private void saveSemesterSubjectRelation(SemesterEntity entity, SubjectEntity subjectEntity) {
        // Verificar se a relação semester-subject já existe
        List<SemesterSubjectEntity> existing = semesterSubjectRepository
                .find("semester.id = ?1 and subject.id = ?2", entity.id, subjectEntity.id)
                .list();

        if (existing.isEmpty()) {
            // Criar a relação semester-subject
            SemesterSubjectEntity relationship = new SemesterSubjectEntity(subjectEntity, entity);
            semesterSubjectRepository.persist(relationship);
        }
    }

    private SubjectEntity saveSingleSubject(Subject subject) {
        // Buscar se a subject já existe por código
        Optional<SubjectEntity> existingSubject = subjectRepository.findByCode(subject.getCode());

        SubjectEntity subjectEntity;
        // Subject existe, então usa a existente
        if (existingSubject.isPresent()) {
            subjectEntity = existingSubject.get();
        } else {
            // Subject não existe, criar nova
            subjectEntity = subject.toEntity();
            subjectRepository.persist(subjectEntity);
            subject.setId(subjectEntity.id);
        }
        return subjectEntity;
    }

    private SemesterEntity saveSemester(Semester semester) {
        SemesterEntity entity = semester.toEntity();
        semesterRepository.persist(entity);
        semester.setId(entity.id);
        return entity;
    }

    @Override
    @Transactional
    public Semester update(Semester semester) {
        Optional<SemesterEntity> optional = semesterRepository.findByIdOptional(semester.getId());
        SemesterEntity entity = optional.orElseThrow(() -> new SemesterNotFoundException(semester.getId()));
        
        entity.setCode(semester.getCode());
        semesterRepository.persist(entity);
        
        return entity.toDomain();
    }

    @Override
    @Transactional
    public Boolean delete(Long semesterId) {
        // Remove os relacionamentos semester-subject primeiro
        semesterSubjectRepository.deleteBySemesterId(semesterId);
        // Remove o semester
        return semesterRepository.deleteById(semesterId);
    }

    @Override
    public Semester findById(Long semesterId) {
        Optional<SemesterEntity> optional = semesterRepository.findByIdOptional(semesterId);
        SemesterEntity entity = optional.orElseThrow(() -> new SemesterNotFoundException(semesterId));
        
        // Busca os subjects relacionados
        List<SemesterSubjectEntity> relationships = semesterSubjectRepository.findBySemesterId(semesterId);
        List<Subject> subjects = relationships.stream()
                .map(rel -> rel.getSubject().toDomain())
                .toList();
        
        Semester semester = entity.toDomain();
        semester.setSubjects(subjects);
        
        return semester;
    }

    @Override
    public List<Semester> findAll() {
        return semesterRepository.findAll().stream()
                .map(SemesterEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Semester addSubjectToSemester(Long semesterId, Long subjectId) {
        Optional<SemesterEntity> semesterOpt = semesterRepository.findByIdOptional(semesterId);
        SemesterEntity semesterEntity = semesterOpt.orElseThrow(() -> new SemesterNotFoundException(semesterId));
        
        Optional<SubjectEntity> subjectOpt = subjectRepository.findByIdOptional(subjectId);
        SubjectEntity subjectEntity = subjectOpt.orElseThrow(() -> new SubjectNotFoundException(subjectId));
        
        // Verifica se a relação já existe
        List<SemesterSubjectEntity> existing = semesterSubjectRepository
                .find("semester.id = ?1 and subject.id = ?2", semesterId, subjectId)
                .list();
        
        if (existing.isEmpty()) {
            SemesterSubjectEntity relationship = new SemesterSubjectEntity(subjectEntity, semesterEntity);
            semesterSubjectRepository.persist(relationship);
        }
        
        return findById(semesterId);
    }

    @Override
    @Transactional
    public Semester removeSubjectFromSemester(Long semesterId, Long subjectId) {
        semesterSubjectRepository.delete("semester.id = ?1 and subject.id = ?2", semesterId, subjectId);
        return findById(semesterId);
    }
}
