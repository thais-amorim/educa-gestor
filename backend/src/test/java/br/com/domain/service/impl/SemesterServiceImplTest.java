package br.com.domain.service.impl;

import br.com.domain.exception.SemesterNotFoundException;
import br.com.domain.exception.SubjectNotFoundException;
import br.com.domain.model.Semester;
import br.com.domain.model.Subject;
import br.com.infrastructure.entity.SemesterEntity;
import br.com.infrastructure.entity.SemesterSubjectEntity;
import br.com.infrastructure.entity.SubjectEntity;
import br.com.infrastructure.repository.SemesterRepository;
import br.com.infrastructure.repository.SemesterSubjectRepository;
import br.com.infrastructure.repository.SubjectRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SemesterServiceImplTest {

    @InjectMocks
    SemesterServiceImpl semesterService;

    @Mock
    SemesterRepository semesterRepository;

    @Mock
    SemesterSubjectRepository semesterSubjectRepository;

    @Mock
    SubjectRepository subjectRepository;

    @Mock
    PanacheQuery<SemesterEntity> panacheQuery;

    @Mock
    PanacheQuery<SemesterSubjectEntity> panacheQuerySemesterSubject;

    private Semester expectedSemester;
    private SemesterEntity expectedSemesterEntity;
    private Subject expectedSubject;
    private SubjectEntity expectedSubjectEntity;
    private SemesterSubjectEntity expectedSemesterSubjectEntity;

    @BeforeEach
    void setUp() {
        expectedSemester = new Semester(1L, "2024.1");
        expectedSemesterEntity = new SemesterEntity("2024.1");
        expectedSemesterEntity.id = 1L;

        expectedSubject = new Subject("CS101", "Computer Science", "Amanda da Silva", 60L);
        expectedSubjectEntity = new SubjectEntity("CS101", "Computer Science", "Amanda da Silva", 60L);

        expectedSemesterSubjectEntity = new SemesterSubjectEntity(expectedSubjectEntity, expectedSemesterEntity);
    }

    @Test
    void whenCreate_ThenSuccess() {
        // Given
        doNothing().when(semesterRepository).persist(any(SemesterEntity.class));

        // When
        Semester obtained = semesterService.create(expectedSemester);

        // Then
        assertNotNull(obtained);
        assertEquals(expectedSemester.getCode(), obtained.getCode());
        verify(semesterRepository, times(1)).persist(any(SemesterEntity.class));
    }

    @Test
    void whenCreate_WithSubjects_ThenSuccess() {
        // Given
        Semester semesterWithoutSubjects = new Semester(null, "2024.1");
        doNothing().when(semesterRepository).persist(any(SemesterEntity.class));

        // When
        Semester obtained = semesterService.create(semesterWithoutSubjects);

        // Then
        assertNotNull(obtained);
        assertEquals("2024.1", obtained.getCode());
        verify(semesterRepository, times(1)).persist(any(SemesterEntity.class));
        verify(semesterSubjectRepository, never()).persist(any(SemesterSubjectEntity.class));
    }

    @Test
    void whenCreate_WithNullSubjects_ThenSuccess() {
        // Given
        Semester semesterWithNullSubjects = new Semester(null, "2024.1");
        semesterWithNullSubjects.setSubjects(null);
        doNothing().when(semesterRepository).persist(any(SemesterEntity.class));

        // When
        Semester obtained = semesterService.create(semesterWithNullSubjects);

        // Then
        assertNotNull(obtained);
        assertEquals("2024.1", obtained.getCode());
        verify(semesterRepository, times(1)).persist(any(SemesterEntity.class));
        verify(semesterSubjectRepository, never()).persist(any(SemesterSubjectEntity.class));
    }

    @Test
    void whenCreate_ThenFailure() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            semesterService.create(null);
        });
        verify(semesterRepository, never()).persist(any(SemesterEntity.class));
    }

    @Test
    void whenUpdate_ThenSuccess() {
        // Given
        Semester semesterToUpdate = new Semester(1L, "2024.2");
        when(semesterRepository.findByIdOptional(1L)).thenReturn(Optional.of(expectedSemesterEntity));
        doNothing().when(semesterRepository).persist(any(SemesterEntity.class));

        // When
        Semester obtained = semesterService.update(semesterToUpdate);

        // Then
        assertNotNull(obtained);
        assertEquals(1L, obtained.getId());
        assertEquals("2024.2", obtained.getCode());
        verify(semesterRepository, times(1)).findByIdOptional(1L);
        verify(semesterRepository, times(1)).persist(any(SemesterEntity.class));
    }

    @Test
    void whenUpdate_ThenFailure_SemesterNotFound() {
        // Given
        Semester semesterToUpdate = new Semester(999L, "2024.2");
        when(semesterRepository.findByIdOptional(999L)).thenReturn(Optional.empty());

        // When & Then
        SemesterNotFoundException exception = assertThrows(SemesterNotFoundException.class, () -> {
            semesterService.update(semesterToUpdate);
        });

        assertEquals("Semester not found with id: 999", exception.getMessage());
        verify(semesterRepository, times(1)).findByIdOptional(999L);
        verify(semesterRepository, never()).persist(any(SemesterEntity.class));
    }

    @Test
    void whenUpdate_ThenFailure_NullSemester() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            semesterService.update(null);
        });
        verify(semesterRepository, never()).findByIdOptional(any());
        verify(semesterRepository, never()).persist(any(SemesterEntity.class));
    }

    @Test
    void whenDelete_ThenSuccess() {
        // Given
        Long semesterId = 1L;
        doNothing().when(semesterSubjectRepository).deleteBySemesterId(semesterId);
        when(semesterRepository.deleteById(semesterId)).thenReturn(true);

        // When
        Boolean obtained = semesterService.delete(semesterId);

        // Then
        assertTrue(obtained);
        verify(semesterSubjectRepository, times(1)).deleteBySemesterId(semesterId);
        verify(semesterRepository, times(1)).deleteById(semesterId);
    }

    @Test
    void whenDelete_ThenFailure_NotFound() {
        // Given
        Long semesterId = 999L;
        doNothing().when(semesterSubjectRepository).deleteBySemesterId(semesterId);
        when(semesterRepository.deleteById(semesterId)).thenReturn(false);

        // When
        Boolean obtained = semesterService.delete(semesterId);

        // Then
        assertFalse(obtained);
        verify(semesterSubjectRepository, times(1)).deleteBySemesterId(semesterId);
        verify(semesterRepository, times(1)).deleteById(semesterId);
    }

    @Test
    void whenDeleteUsingNullId_ThenFailure() {
        // Given
        doNothing().when(semesterSubjectRepository).deleteBySemesterId(null);
        when(semesterRepository.deleteById(null)).thenReturn(false);

        // When
        Boolean obtained = semesterService.delete(null);

        // Then
        assertFalse(obtained);
        verify(semesterSubjectRepository, times(1)).deleteBySemesterId(null);
        verify(semesterRepository, times(1)).deleteById(null);
    }

    @Test
    void whenFindById_ThenSuccess() {
        // Given
        Long semesterId = 1L;
        when(semesterRepository.findByIdOptional(semesterId)).thenReturn(Optional.of(expectedSemesterEntity));
        when(semesterSubjectRepository.findBySemesterId(semesterId)).thenReturn(Arrays.asList(expectedSemesterSubjectEntity));

        // When
        Semester obtained = semesterService.findById(semesterId);

        // Then
        assertNotNull(obtained);
        assertEquals(expectedSemester.getId(), obtained.getId());
        assertEquals(expectedSemester.getCode(), obtained.getCode());
        assertNotNull(obtained.getSubjects());
        assertEquals(1, obtained.getSubjects().size());
        assertEquals(expectedSubject.getCode(), obtained.getSubjects().get(0).getCode());
        verify(semesterRepository, times(1)).findByIdOptional(semesterId);
        verify(semesterSubjectRepository, times(1)).findBySemesterId(semesterId);
    }

    @Test
    void whenFindById_ThenFailureNotFound() {
        // Given
        Long semesterId = 999L;
        when(semesterRepository.findByIdOptional(semesterId)).thenReturn(Optional.empty());

        // When & Then
        SemesterNotFoundException exception = assertThrows(SemesterNotFoundException.class, () -> {
            semesterService.findById(semesterId);
        });

        assertEquals("Semester not found with id: " + semesterId, exception.getMessage());
        verify(semesterRepository, times(1)).findByIdOptional(semesterId);
        verify(semesterSubjectRepository, never()).findBySemesterId(semesterId);
    }

    @Test
    void whenFindByIdUsingNullId_ThenFailure() {
        // Given
        when(semesterRepository.findByIdOptional(null)).thenReturn(Optional.empty());

        // When & Then
        SemesterNotFoundException exception = assertThrows(SemesterNotFoundException.class, () -> {
            semesterService.findById(null);
        });

        assertEquals("Semester not found with id: null", exception.getMessage());
        verify(semesterRepository, times(1)).findByIdOptional(null);
        verify(semesterSubjectRepository, never()).findBySemesterId(null);
    }

    @Test
    void whenFindAll_ThenSuccess() {
        // Given
        SemesterEntity entity1 = new SemesterEntity("2024.1");
        entity1.id = 1L;
        SemesterEntity entity2 = new SemesterEntity("2024.2");
        entity2.id = 2L;
        List<SemesterEntity> entities = Arrays.asList(entity1, entity2);

        when(semesterRepository.findAll()).thenReturn(panacheQuery);
        when(panacheQuery.stream()).thenReturn(entities.stream());

        // When
        List<Semester> obtained = semesterService.findAll();

        // Then
        assertNotNull(obtained);
        assertEquals(2, obtained.size());

        assertEquals(1L, obtained.get(0).getId());
        assertEquals("2024.1", obtained.get(0).getCode());

        assertEquals(2L, obtained.get(1).getId());
        assertEquals("2024.2", obtained.get(1).getCode());

        verify(semesterRepository, times(1)).findAll();
    }

    @Test
    void whenFindAll_EmptyList() {
        // Given
        when(semesterRepository.findAll()).thenReturn(panacheQuery);
        when(panacheQuery.stream()).thenReturn(List.<SemesterEntity>of().stream());

        // When
        List<Semester> obtained = semesterService.findAll();

        // Then
        assertNotNull(obtained);
        assertTrue(obtained.isEmpty());
        verify(semesterRepository, times(1)).findAll();
    }

    @Test
    void whenFindAll_SingleSemester() {
        // Given
        List<SemesterEntity> entities = Arrays.asList(expectedSemesterEntity);
        when(semesterRepository.findAll()).thenReturn(panacheQuery);
        when(panacheQuery.stream()).thenReturn(entities.stream());

        // When
        List<Semester> result = semesterService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedSemester.getId(), result.get(0).getId());
        assertEquals(expectedSemester.getCode(), result.get(0).getCode());
        verify(semesterRepository, times(1)).findAll();
    }

    @Test
    void whenAddSubjectToSemester_ThenSuccess() {
        // Given
        Long semesterId = 1L;
        String subjectCode = "CS101";
        
        when(semesterRepository.findByIdOptional(semesterId)).thenReturn(Optional.of(expectedSemesterEntity));
        when(subjectRepository.findByIdOptional(subjectCode)).thenReturn(Optional.of(expectedSubjectEntity));
        when(semesterSubjectRepository.find(eq("semester.id = ?1 and subject.code = ?2"), eq(semesterId), eq(subjectCode))).thenReturn(panacheQuerySemesterSubject);
        when(panacheQuerySemesterSubject.list()).thenReturn(Arrays.asList());
        doNothing().when(semesterSubjectRepository).persist(any(SemesterSubjectEntity.class));
        
        // Mock findById call (called at the end of addSubjectToSemester)
        when(semesterSubjectRepository.findBySemesterId(semesterId)).thenReturn(Arrays.asList(expectedSemesterSubjectEntity));

        // When
        Semester obtained = semesterService.addSubjectToSemester(semesterId, subjectCode);

        // Then
        assertNotNull(obtained);
        assertEquals(semesterId, obtained.getId());
        assertEquals(expectedSemesterEntity.getCode(), obtained.getCode());
        verify(semesterRepository, times(2)).findByIdOptional(semesterId); // called twice: once in addSubjectToSemester, once in findById
        verify(subjectRepository, times(1)).findByIdOptional(subjectCode);
        verify(semesterSubjectRepository, times(1)).persist(any(SemesterSubjectEntity.class));
    }

    @Test
    void whenAddSubjectToSemester_RelationshipAlreadyExists_ThenSuccess() {
        // Given
        Long semesterId = 1L;
        String subjectCode = "CS101";
        
        when(semesterRepository.findByIdOptional(semesterId)).thenReturn(Optional.of(expectedSemesterEntity));
        when(subjectRepository.findByIdOptional(subjectCode)).thenReturn(Optional.of(expectedSubjectEntity));
        when(semesterSubjectRepository.find(eq("semester.id = ?1 and subject.code = ?2"), eq(semesterId), eq(subjectCode))).thenReturn(panacheQuerySemesterSubject);
        when(panacheQuerySemesterSubject.list()).thenReturn(Arrays.asList(expectedSemesterSubjectEntity)); // relationship already exists
        
        // Mock findById call
        when(semesterSubjectRepository.findBySemesterId(semesterId)).thenReturn(Arrays.asList(expectedSemesterSubjectEntity));

        // When
        Semester obtained = semesterService.addSubjectToSemester(semesterId, subjectCode);

        // Then
        assertNotNull(obtained);
        assertEquals(semesterId, obtained.getId());
        verify(semesterRepository, times(2)).findByIdOptional(semesterId);
        verify(subjectRepository, times(1)).findByIdOptional(subjectCode);
        verify(semesterSubjectRepository, never()).persist(any(SemesterSubjectEntity.class)); // should not persist since relationship exists
    }

    @Test
    void whenAddSubjectToSemester_SemesterNotFound_ThenFailure() {
        // Given
        Long semesterId = 999L;
        String subjectCode = "CS101";
        
        when(semesterRepository.findByIdOptional(semesterId)).thenReturn(Optional.empty());

        // When & Then
        SemesterNotFoundException exception = assertThrows(SemesterNotFoundException.class, () -> {
            semesterService.addSubjectToSemester(semesterId, subjectCode);
        });

        assertEquals("Semester not found with id: " + semesterId, exception.getMessage());
        verify(semesterRepository, times(1)).findByIdOptional(semesterId);
        verify(subjectRepository, never()).findByIdOptional(subjectCode);
        verify(semesterSubjectRepository, never()).persist(any(SemesterSubjectEntity.class));
    }

    @Test
    void whenAddSubjectToSemester_SubjectNotFound_ThenFailure() {
        // Given
        Long semesterId = 1L;
        String subjectCode = "UNKNOWN";
        
        when(semesterRepository.findByIdOptional(semesterId)).thenReturn(Optional.of(expectedSemesterEntity));
        when(subjectRepository.findByIdOptional(subjectCode)).thenReturn(Optional.empty());

        // When & Then
        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class, () -> {
            semesterService.addSubjectToSemester(semesterId, subjectCode);
        });

        assertEquals("Subject not found for code: " + subjectCode, exception.getMessage());
        verify(semesterRepository, times(1)).findByIdOptional(semesterId);
        verify(subjectRepository, times(1)).findByIdOptional(subjectCode);
        verify(semesterSubjectRepository, never()).persist(any(SemesterSubjectEntity.class));
    }

    @Test
    void whenRemoveSubjectFromSemester_ThenSuccess() {
        // Given
        Long semesterId = 1L;
        String subjectCode = "CS101";
        
        when(semesterSubjectRepository.delete(eq("semester.id = ?1 and subject.code = ?2"), eq(semesterId), eq(subjectCode))).thenReturn(1L);
        
        // Mock findById call
        when(semesterRepository.findByIdOptional(semesterId)).thenReturn(Optional.of(expectedSemesterEntity));
        when(semesterSubjectRepository.findBySemesterId(semesterId)).thenReturn(Arrays.asList());

        // When
        Semester obtained = semesterService.removeSubjectFromSemester(semesterId, subjectCode);

        // Then
        assertNotNull(obtained);
        assertEquals(semesterId, obtained.getId());
        assertEquals(expectedSemesterEntity.getCode(), obtained.getCode());
        verify(semesterSubjectRepository, times(1)).delete(eq("semester.id = ?1 and subject.code = ?2"), eq(semesterId), eq(subjectCode));
        verify(semesterRepository, times(1)).findByIdOptional(semesterId);
    }

    @Test
    void whenRemoveSubjectFromSemester_SemesterNotFoundInFindById_ThenFailure() {
        // Given
        Long semesterId = 999L;
        String subjectCode = "CS101";
        
        when(semesterSubjectRepository.delete(eq("semester.id = ?1 and subject.code = ?2"), eq(semesterId), eq(subjectCode))).thenReturn(0L);
        when(semesterRepository.findByIdOptional(semesterId)).thenReturn(Optional.empty());

        // When & Then
        SemesterNotFoundException exception = assertThrows(SemesterNotFoundException.class, () -> {
            semesterService.removeSubjectFromSemester(semesterId, subjectCode);
        });

        assertEquals("Semester not found with id: " + semesterId, exception.getMessage());
        verify(semesterSubjectRepository, times(1)).delete(eq("semester.id = ?1 and subject.code = ?2"), eq(semesterId), eq(subjectCode));
        verify(semesterRepository, times(1)).findByIdOptional(semesterId);
    }
}
