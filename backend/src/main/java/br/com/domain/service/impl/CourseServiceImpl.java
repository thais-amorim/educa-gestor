package br.com.domain.service.impl;

import br.com.domain.exception.CourseNotFoundException;
import br.com.domain.exception.SemesterNotFoundException;
import br.com.domain.model.Course;
import br.com.domain.model.Semester;
import br.com.domain.service.CourseService;
import br.com.infrastructure.entity.CourseEntity;
import br.com.infrastructure.entity.CourseSemesterEntity;
import br.com.infrastructure.entity.SemesterEntity;
import br.com.infrastructure.repository.CourseRepository;
import br.com.infrastructure.repository.CourseSemesterRepository;
import br.com.infrastructure.repository.SemesterRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CourseServiceImpl implements CourseService {
    
    @Inject
    CourseRepository courseRepository;
    
    @Inject
    CourseSemesterRepository courseSemesterRepository;
    
    @Inject
    SemesterRepository semesterRepository;

    @Override
    @Transactional
    public Course create(Course course) {
        CourseEntity entity = saveCourse(course);
        saveRelatedSemesters(course, entity);
        return course;
    }

    private void saveRelatedSemesters(Course course, CourseEntity courseEntity) {
        if (course.getSemesters() != null && !course.getSemesters().isEmpty()) {
            for (Semester semester : course.getSemesters()) {
                SemesterEntity semesterEntity = saveSingleSemester(semester);
                saveCourseSemesterRelation(courseEntity, semesterEntity);
            }
        }
    }

    private void saveCourseSemesterRelation(CourseEntity courseEntity, SemesterEntity semesterEntity) {
        // Verificar se a relação course-semester já existe
        List<CourseSemesterEntity> existing = courseSemesterRepository
                .find("course.id = ?1 and semester.id = ?2", courseEntity.id, semesterEntity.id)
                .list();

        if (existing.isEmpty()) {
            // Criar a relação course-semester
            CourseSemesterEntity relationship = new CourseSemesterEntity(courseEntity, semesterEntity);
            courseSemesterRepository.persist(relationship);
        }
    }

    private SemesterEntity saveSingleSemester(Semester semester) {
        // Buscar se o semester já existe por código
        Optional<SemesterEntity> existingSemester = semesterRepository.findByCode(semester.getCode());

        SemesterEntity semesterEntity;
        // Semester existe, então usa o existente
        if (existingSemester.isPresent()) {
            semesterEntity = existingSemester.get();
        } else {
            // Semester não existe, criar novo
            semesterEntity = semester.toEntity();
            semesterRepository.persist(semesterEntity);
            semester.setId(semesterEntity.id);
        }
        return semesterEntity;
    }

    private CourseEntity saveCourse(Course course) {
        CourseEntity entity = course.toEntity();
        courseRepository.persist(entity);
        course.setId(entity.id);
        return entity;
    }

    @Override
    @Transactional
    public Course update(Course course) {
        Optional<CourseEntity> optional = courseRepository.findByIdOptional(course.getId());
        CourseEntity entity = optional.orElseThrow(() -> new CourseNotFoundException(course.getId()));
        
        entity.setCode(course.getCode());
        entity.setName(course.getName());
        courseRepository.persist(entity);
        
        return entity.toDomain();
    }

    @Override
    @Transactional
    public Boolean delete(Long courseId) {
        // Remove os relacionamentos course-semester primeiro
        courseSemesterRepository.deleteByCourseId(courseId);
        // Remove o course
        return courseRepository.deleteById(courseId);
    }

    @Override
    public Course findById(Long courseId) {
        Optional<CourseEntity> optional = courseRepository.findByIdOptional(courseId);
        CourseEntity entity = optional.orElseThrow(() -> new CourseNotFoundException(courseId));
        
        // Busca os semesters relacionados
        List<CourseSemesterEntity> relationships = courseSemesterRepository.findByCourseId(courseId);
        List<Semester> semesters = relationships.stream()
                .map(rel -> rel.getSemester().toDomain())
                .toList();
        
        Course course = entity.toDomain();
        course.setSemesters(semesters);
        
        return course;
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll().stream()
                .map(CourseEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Course addSemesterToCourse(Long courseId, Long semesterId) {
        Optional<CourseEntity> courseOpt = courseRepository.findByIdOptional(courseId);
        CourseEntity courseEntity = courseOpt.orElseThrow(() -> new CourseNotFoundException(courseId));
        
        Optional<SemesterEntity> semesterOpt = semesterRepository.findByIdOptional(semesterId);
        SemesterEntity semesterEntity = semesterOpt.orElseThrow(() -> new SemesterNotFoundException(semesterId));
        
        // Verifica se a relação já existe
        List<CourseSemesterEntity> existing = courseSemesterRepository
                .find("course.id = ?1 and semester.id = ?2", courseId, semesterId)
                .list();
        
        if (existing.isEmpty()) {
            CourseSemesterEntity relationship = new CourseSemesterEntity(courseEntity, semesterEntity);
            courseSemesterRepository.persist(relationship);
        }
        
        return findById(courseId);
    }

    @Override
    @Transactional
    public Course removeSemesterFromCourse(Long courseId, Long semesterId) {
        courseSemesterRepository.delete("course.id = ?1 and semester.id = ?2", courseId, semesterId);
        return findById(courseId);
    }
}
