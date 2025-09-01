package br.com.domain.service;

import br.com.domain.model.Semester;

import java.util.List;

public interface SemesterService {
    public Semester create(Semester semester);
    public Semester update(Semester semester);
    public Boolean delete(Long semesterId);
    public Semester findById(Long semesterId);
    public List<Semester> findAll();
    public Semester addSubjectToSemester(Long semesterId, String subjectCode);
    public Semester removeSubjectFromSemester(Long semesterId, String subjectCode);
}
