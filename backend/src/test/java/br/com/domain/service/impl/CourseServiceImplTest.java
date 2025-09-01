package br.com.domain.service.impl;

import br.com.domain.exception.CourseNotFoundException;
import br.com.domain.exception.SemesterNotFoundException;
import br.com.domain.model.Course;
import br.com.domain.model.Semester;
import br.com.infrastructure.entity.CourseEntity;
import br.com.infrastructure.entity.CourseSemesterEntity;
import br.com.infrastructure.entity.SemesterEntity;
import br.com.infrastructure.repository.CourseRepository;
import br.com.infrastructure.repository.CourseSemesterRepository;
import br.com.infrastructure.repository.SemesterRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @InjectMocks
    CourseServiceImpl courseService;

    @Mock
    CourseRepository courseRepository;

    @Mock
    CourseSemesterRepository courseSemesterRepository;

    @Mock
    SemesterRepository semesterRepository;

    @Mock
    PanacheQuery<CourseEntity> panacheQuery;

    @Mock
    PanacheQuery<CourseSemesterEntity> courseSemesterPanacheQuery;

    private Course expectedCourse;
    private CourseEntity expectedCourseEntity;
    private Semester expectedSemester;
    private SemesterEntity expectedSemesterEntity;
    private CourseSemesterEntity expectedCourseSemesterEntity;

    @BeforeEach
    void setUp() {
        expectedCourse = new Course("CS", "Computer Science");
        expectedCourse.setId(1L);
        
        expectedCourseEntity = new CourseEntity("CS", "Computer Science");
        expectedCourseEntity.id = 1L;
        
        expectedSemester = new Semester(1L, "2024.1");
        expectedSemesterEntity = new SemesterEntity("2024.1");
        expectedSemesterEntity.id = 1L;
        
        expectedCourseSemesterEntity = new CourseSemesterEntity(expectedCourseEntity, expectedSemesterEntity);
    }

    @Test
    void whenCreate_ThenSuccess() {
        // Given
        doNothing().when(courseRepository).persist(any(CourseEntity.class));

        // When
        Course obtained = courseService.create(expectedCourse);

        // Then
        assertNotNull(obtained);
        assertEquals(expectedCourse.getCode(), obtained.getCode());
        assertEquals(expectedCourse.getName(), obtained.getName());
        verify(courseRepository, times(1)).persist(any(CourseEntity.class));
    }

    @Test
    void whenCreate_WithSemesters_ThenSuccess() {
        // Given
        List<Semester> semesters = Arrays.asList(expectedSemester);
        expectedCourse.setSemesters(semesters);
        
        doAnswer(invocation -> {
            CourseEntity entity = invocation.getArgument(0);
            entity.id = 1L; // Simula a atribuição do ID
            return null;
        }).when(courseRepository).persist(any(CourseEntity.class));
        
        when(semesterRepository.findByCode("2024.1")).thenReturn(Optional.empty());
        
        doAnswer(invocation -> {
            SemesterEntity entity = invocation.getArgument(0);
            entity.id = 1L; // Simula a atribuição do ID
            return null;
        }).when(semesterRepository).persist(any(SemesterEntity.class));
        
        when(courseSemesterRepository.find("course.id = ?1 and semester.id = ?2", 1L, 1L)).thenReturn(courseSemesterPanacheQuery);
        when(courseSemesterPanacheQuery.list()).thenReturn(Collections.emptyList());
        doNothing().when(courseSemesterRepository).persist(any(CourseSemesterEntity.class));

        // When
        Course obtained = courseService.create(expectedCourse);

        // Then
        assertNotNull(obtained);
        assertEquals(expectedCourse.getCode(), obtained.getCode());
        assertEquals(expectedCourse.getName(), obtained.getName());
        verify(courseRepository, times(1)).persist(any(CourseEntity.class));
        verify(semesterRepository, times(1)).persist(any(SemesterEntity.class));
        verify(courseSemesterRepository, times(1)).persist(any(CourseSemesterEntity.class));
    }

    @Test
    void whenCreate_WithExistingSemester_ThenSuccess() {
        // Given
        List<Semester> semesters = Arrays.asList(expectedSemester);
        expectedCourse.setSemesters(semesters);
        
        doAnswer(invocation -> {
            CourseEntity entity = invocation.getArgument(0);
            entity.id = 1L; // Simula a atribuição do ID
            return null;
        }).when(courseRepository).persist(any(CourseEntity.class));
        
        when(semesterRepository.findByCode("2024.1")).thenReturn(Optional.of(expectedSemesterEntity));
        when(courseSemesterRepository.find("course.id = ?1 and semester.id = ?2", 1L, 1L)).thenReturn(courseSemesterPanacheQuery);
        when(courseSemesterPanacheQuery.list()).thenReturn(Collections.emptyList());
        doNothing().when(courseSemesterRepository).persist(any(CourseSemesterEntity.class));

        // When
        Course obtained = courseService.create(expectedCourse);

        // Then
        assertNotNull(obtained);
        assertEquals(expectedCourse.getCode(), obtained.getCode());
        assertEquals(expectedCourse.getName(), obtained.getName());
        verify(courseRepository, times(1)).persist(any(CourseEntity.class));
        verify(semesterRepository, never()).persist(any(SemesterEntity.class));
        verify(courseSemesterRepository, times(1)).persist(any(CourseSemesterEntity.class));
    }

    @Test
    void whenCreate_ThenFailure() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            courseService.create(null);
        });
        verify(courseRepository, never()).persist(any(CourseEntity.class));
    }

    @Test
    void whenUpdate_ThenSuccess() {
        // Given
        Course courseToUpdate = new Course(1L, "CS", "Updated Computer Science");
        when(courseRepository.findByIdOptional(1L)).thenReturn(Optional.of(expectedCourseEntity));
        doNothing().when(courseRepository).persist(any(CourseEntity.class));

        // When
        Course obtained = courseService.update(courseToUpdate);

        // Then
        assertNotNull(obtained);
        assertEquals("CS", obtained.getCode());
        assertEquals("Updated Computer Science", obtained.getName());
        verify(courseRepository, times(1)).findByIdOptional(1L);
        verify(courseRepository, times(1)).persist(any(CourseEntity.class));
    }

    @Test
    void whenUpdate_ThenFailure_CourseNotFound() {
        // Given
        Course courseToUpdate = new Course(999L, "UNKNOWN", "Unknown Course");
        when(courseRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        // When & Then
        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> {
            courseService.update(courseToUpdate);
        });

        assertEquals("Course not found with ID: 999", exception.getMessage());
        verify(courseRepository, times(1)).findByIdOptional(999L);
        verify(courseRepository, never()).persist(any(CourseEntity.class));
    }

    @Test
    void whenUpdate_ThenFailure_NullCourse() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            courseService.update(null);
        });
        verify(courseRepository, never()).findByIdOptional(any());
        verify(courseRepository, never()).persist(any(CourseEntity.class));
    }

    @Test
    void whenDelete_ThenSuccess() {
        // Given
        Long courseId = 1L;
        doNothing().when(courseSemesterRepository).deleteByCourseId(courseId);
        when(courseRepository.deleteById(courseId)).thenReturn(true);

        // When
        Boolean obtained = courseService.delete(courseId);

        // Then
        assertTrue(obtained);
        verify(courseSemesterRepository, times(1)).deleteByCourseId(courseId);
        verify(courseRepository, times(1)).deleteById(courseId);
    }

    @Test
    void whenDelete_ThenFailure_NotFound() {
        // Given
        Long courseId = 999L;
        doNothing().when(courseSemesterRepository).deleteByCourseId(courseId);
        when(courseRepository.deleteById(courseId)).thenReturn(false);

        // When
        Boolean obtained = courseService.delete(courseId);

        // Then
        assertFalse(obtained);
        verify(courseSemesterRepository, times(1)).deleteByCourseId(courseId);
        verify(courseRepository, times(1)).deleteById(courseId);
    }

    @Test
    void whenDeleteUsingNullId_ThenFailure() {
        // Given
        doNothing().when(courseSemesterRepository).deleteByCourseId(null);
        when(courseRepository.deleteById(null)).thenReturn(false);

        // When
        Boolean obtained = courseService.delete(null);

        // Then
        assertFalse(obtained);
        verify(courseSemesterRepository, times(1)).deleteByCourseId(null);
        verify(courseRepository, times(1)).deleteById(null);
    }

    @Test
    void whenFindById_ThenSuccess() {
        // Given
        Long courseId = 1L;
        List<CourseSemesterEntity> relationships = Arrays.asList(expectedCourseSemesterEntity);
        
        when(courseRepository.findByIdOptional(courseId)).thenReturn(Optional.of(expectedCourseEntity));
        when(courseSemesterRepository.findByCourseId(courseId)).thenReturn(relationships);

        // When
        Course obtained = courseService.findById(courseId);

        // Then
        assertNotNull(obtained);
        assertEquals(expectedCourse.getCode(), obtained.getCode());
        assertEquals(expectedCourse.getName(), obtained.getName());
        assertNotNull(obtained.getSemesters());
        assertEquals(1, obtained.getSemesters().size());
        assertEquals("2024.1", obtained.getSemesters().get(0).getCode());
        verify(courseRepository, times(1)).findByIdOptional(courseId);
        verify(courseSemesterRepository, times(1)).findByCourseId(courseId);
    }

    @Test
    void whenFindById_ThenFailureNotFound() {
        // Given
        Long courseId = 999L;
        when(courseRepository.findByIdOptional(courseId)).thenReturn(Optional.empty());

        // When & Then
        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> {
            courseService.findById(courseId);
        });

        assertEquals("Course not found with ID: " + courseId, exception.getMessage());
        verify(courseRepository, times(1)).findByIdOptional(courseId);
        verify(courseSemesterRepository, never()).findByCourseId(any());
    }

    @Test
    void whenFindByIdUsingNullId_ThenFailure() {
        // Given
        when(courseRepository.findByIdOptional(null)).thenReturn(Optional.empty());

        // When & Then
        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> {
            courseService.findById(null);
        });

        assertEquals("Course not found with ID: null", exception.getMessage());
        verify(courseRepository, times(1)).findByIdOptional(null);
    }

    @Test
    void testFindAll_ThenSuccess() {
        // Given
        CourseEntity entity1 = new CourseEntity("CS", "Computer Science");
        CourseEntity entity2 = new CourseEntity("MATH", "Mathematics");
        List<CourseEntity> entities = Arrays.asList(entity1, entity2);
        
        when(courseRepository.findAll()).thenReturn(panacheQuery);
        when(panacheQuery.stream()).thenReturn(entities.stream());

        // When
        List<Course> obtained = courseService.findAll();

        // Then
        assertNotNull(obtained);
        assertEquals(2, obtained.size());
        
        assertEquals("CS", obtained.get(0).getCode());
        assertEquals("Computer Science", obtained.get(0).getName());
        
        assertEquals("MATH", obtained.get(1).getCode());
        assertEquals("Mathematics", obtained.get(1).getName());
        
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(courseRepository.findAll()).thenReturn(panacheQuery);
        when(panacheQuery.stream()).thenReturn(List.<CourseEntity>of().stream());

        // When
        List<Course> obtained = courseService.findAll();

        // Then
        assertNotNull(obtained);
        assertTrue(obtained.isEmpty());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_SingleCourse() {
        // Given
        List<CourseEntity> entities = Arrays.asList(expectedCourseEntity);
        when(courseRepository.findAll()).thenReturn(panacheQuery);
        when(panacheQuery.stream()).thenReturn(entities.stream());

        // When
        List<Course> result = courseService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedCourse.getCode(), result.get(0).getCode());
        assertEquals(expectedCourse.getName(), result.get(0).getName());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void whenAddSemesterToCourse_ThenSuccess() {
        // Given
        Long courseId = 1L;
        Long semesterId = 1L;
        
        when(courseRepository.findByIdOptional(courseId)).thenReturn(Optional.of(expectedCourseEntity));
        when(semesterRepository.findByIdOptional(semesterId)).thenReturn(Optional.of(expectedSemesterEntity));
        when(courseSemesterRepository.find("course.id = ?1 and semester.id = ?2", courseId, semesterId)).thenReturn(courseSemesterPanacheQuery);
        when(courseSemesterPanacheQuery.list()).thenReturn(Collections.emptyList());
        doNothing().when(courseSemesterRepository).persist(any(CourseSemesterEntity.class));
        
        // Mock findById call - chamado duas vezes: uma no addSemesterToCourse e outra no findById interno
        List<CourseSemesterEntity> relationships = Arrays.asList(expectedCourseSemesterEntity);
        when(courseSemesterRepository.findByCourseId(courseId)).thenReturn(relationships);

        // When
        Course obtained = courseService.addSemesterToCourse(courseId, semesterId);

        // Then
        assertNotNull(obtained);
        assertEquals(expectedCourse.getCode(), obtained.getCode());
        assertEquals(expectedCourse.getName(), obtained.getName());
        verify(courseRepository, times(2)).findByIdOptional(courseId); // Chamado 2 vezes: addSemester + findById
        verify(semesterRepository, times(1)).findByIdOptional(semesterId);
        verify(courseSemesterRepository, times(1)).persist(any(CourseSemesterEntity.class));
    }

    @Test
    void whenAddSemesterToCourse_WithExistingRelation_ThenSuccess() {
        // Given
        Long courseId = 1L;
        Long semesterId = 1L;
        List<CourseSemesterEntity> existingRelations = Arrays.asList(expectedCourseSemesterEntity);
        
        when(courseRepository.findByIdOptional(courseId)).thenReturn(Optional.of(expectedCourseEntity));
        when(semesterRepository.findByIdOptional(semesterId)).thenReturn(Optional.of(expectedSemesterEntity));
        when(courseSemesterRepository.find("course.id = ?1 and semester.id = ?2", courseId, semesterId)).thenReturn(courseSemesterPanacheQuery);
        when(courseSemesterPanacheQuery.list()).thenReturn(existingRelations);
        
        // Mock findById call
        when(courseSemesterRepository.findByCourseId(courseId)).thenReturn(existingRelations);

        // When
        Course obtained = courseService.addSemesterToCourse(courseId, semesterId);

        // Then
        assertNotNull(obtained);
        assertEquals(expectedCourse.getCode(), obtained.getCode());
        assertEquals(expectedCourse.getName(), obtained.getName());
        verify(courseRepository, times(2)).findByIdOptional(courseId); // Chamado 2 vezes: addSemester + findById
        verify(semesterRepository, times(1)).findByIdOptional(semesterId);
        verify(courseSemesterRepository, never()).persist(any(CourseSemesterEntity.class));
    }

    @Test
    void whenAddSemesterToCourse_ThenFailure_CourseNotFound() {
        // Given
        Long courseId = 999L;
        Long semesterId = 1L;
        
        when(courseRepository.findByIdOptional(courseId)).thenReturn(Optional.empty());

        // When & Then
        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> {
            courseService.addSemesterToCourse(courseId, semesterId);
        });

        assertEquals("Course not found with ID: " + courseId, exception.getMessage());
        verify(courseRepository, times(1)).findByIdOptional(courseId);
        verify(semesterRepository, never()).findByIdOptional(any());
    }

    @Test
    void whenAddSemesterToCourse_ThenFailure_SemesterNotFound() {
        // Given
        Long courseId = 1L;
        Long semesterId = 999L;
        
        when(courseRepository.findByIdOptional(courseId)).thenReturn(Optional.of(expectedCourseEntity));
        when(semesterRepository.findByIdOptional(semesterId)).thenReturn(Optional.empty());

        // When & Then
        SemesterNotFoundException exception = assertThrows(SemesterNotFoundException.class, () -> {
            courseService.addSemesterToCourse(courseId, semesterId);
        });

        assertEquals("Semester not found with id: " + semesterId, exception.getMessage());
        verify(courseRepository, times(1)).findByIdOptional(courseId);
        verify(semesterRepository, times(1)).findByIdOptional(semesterId);
    }

    @Test
    void whenRemoveSemesterFromCourse_ThenSuccess() {
        // Given
        Long courseId = 1L;
        Long semesterId = 1L;
        
        when(courseSemesterRepository.delete("course.id = ?1 and semester.id = ?2", courseId, semesterId)).thenReturn(1L);
        
        // Mock findById call
        List<CourseSemesterEntity> relationships = Collections.emptyList();
        when(courseRepository.findByIdOptional(courseId)).thenReturn(Optional.of(expectedCourseEntity));
        when(courseSemesterRepository.findByCourseId(courseId)).thenReturn(relationships);

        // When
        Course obtained = courseService.removeSemesterFromCourse(courseId, semesterId);

        // Then
        assertNotNull(obtained);
        assertEquals(expectedCourse.getCode(), obtained.getCode());
        assertEquals(expectedCourse.getName(), obtained.getName());
        assertTrue(obtained.getSemesters().isEmpty());
        verify(courseSemesterRepository, times(1)).delete("course.id = ?1 and semester.id = ?2", courseId, semesterId);
    }
}
