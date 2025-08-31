package br.com.domain.service;

import br.com.domain.model.Subject;

import java.util.List;

public interface SubjectService {
    public Subject create(Subject subject);
    public Subject update(Subject subject);
    public Boolean delete(Long subjectId);
    public Subject findById(Long subjectId);
    public List<Subject> findAll();
}
