package br.com.domain.service.impl;

import br.com.domain.exception.SubjectNotFoundException;
import br.com.domain.model.Subject;
import br.com.infrastructure.entity.SubjectEntity;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceImplTest {

    @InjectMocks
    SubjectServiceImpl subjectService;

    @Mock
    SubjectRepository subjectRepository;

    @Mock
    PanacheQuery<SubjectEntity> panacheQuery;

    private Subject expectedSubject;
    private SubjectEntity expectedSubjectEntity;

    @BeforeEach
    void setUp() {
        expectedSubject = new Subject("CS101", "Computer Science", "Amanda da Silva", 60L);
        expectedSubjectEntity = new SubjectEntity("CS101", "Computer Science", "Amanda da Silva", 60L);
    }

    @Test
    void whenCreate_ThenSuccess() {
        // Given
        doNothing().when(subjectRepository).persist(any(SubjectEntity.class));

        // When
        Subject obtained = subjectService.create(expectedSubject);

        // Then
        assertNotNull(obtained);
        assertEquals(expectedSubject.getCode(), obtained.getCode());
        assertEquals(expectedSubject.getName(), obtained.getName());
        assertEquals(expectedSubject.getInstructorName(), obtained.getInstructorName());
        assertEquals(expectedSubject.getWorkload(), obtained.getWorkload());
        verify(subjectRepository, times(1)).persist(any(SubjectEntity.class));
    }

    @Test
    void whenCreate_ThenFailure() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            subjectService.create(null);
        });
        verify(subjectRepository, never()).persist(any(SubjectEntity.class));
    }

    @Test
    void whenDelete_ThenSuccess() {
        // Given
        String subjectCode = "CS101";
        when(subjectRepository.deleteById(subjectCode)).thenReturn(true);

        // When
        Boolean obtained = subjectService.delete(subjectCode);

        // Then
        assertTrue(obtained);
        verify(subjectRepository, times(1)).deleteById(subjectCode);
    }

    @Test
    void whenDelete_ThenFailure_NotFound() {
        // Given
        String subjectCode = "NOT_SAVED_CODE";
        when(subjectRepository.deleteById(subjectCode)).thenReturn(false);

        // When
        Boolean obtained = subjectService.delete(subjectCode);

        // Then
        assertFalse(obtained);
        verify(subjectRepository, times(1)).deleteById(subjectCode);
    }

    @Test
    void whenDeleteUsingNullCode_ThenFailure() {
        // Given
        when(subjectRepository.deleteById(null)).thenReturn(false);

        // When
        Boolean obtained = subjectService.delete(null);

        // Then
        assertFalse(obtained);
        verify(subjectRepository, times(1)).deleteById(null);
    }

    @Test
    void whenFindByCode_ThenSuccess() {
        // Given
        String subjectCode = "CS101";
        when(subjectRepository.findByIdOptional(subjectCode)).thenReturn(Optional.of(expectedSubjectEntity));

        // When
        Subject obtained = subjectService.findByCode(subjectCode);

        // Then
        assertNotNull(obtained);
        assertEquals(expectedSubject.getCode(), obtained.getCode());
        assertEquals(expectedSubject.getName(), obtained.getName());
        assertEquals(expectedSubject.getInstructorName(), obtained.getInstructorName());
        assertEquals(expectedSubject.getWorkload(), obtained.getWorkload());
        verify(subjectRepository, times(1)).findByIdOptional(subjectCode);
    }

    @Test
    void whenFindByCode_ThenFailureNotFound() {
        // Given
        String subjectCode = "NOT_SAVED_CODE";
        when(subjectRepository.findByIdOptional(subjectCode)).thenReturn(Optional.empty());

        // When & Then
        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class, () -> {
            subjectService.findByCode(subjectCode);
        });

        assertEquals("Subject not found for code: " + subjectCode, exception.getMessage());
        verify(subjectRepository, times(1)).findByIdOptional(subjectCode);
    }

    @Test
    void whenFindByCodeUsingNullCode_ThenFailure() {
        // Given
        when(subjectRepository.findByIdOptional(null)).thenReturn(Optional.empty());

        // When & Then
        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class, () -> {
            subjectService.findByCode(null);
        });

        assertEquals("Subject not found for code: null", exception.getMessage());
        verify(subjectRepository, times(1)).findByIdOptional(null);
    }

    @Test
    void whenUpdate_ThenSuccess() {
        // Given
        Subject subjectToUpdate = new Subject("CS101", "Updated Computer Science", "João Pereira", 80L);
        SubjectEntity existingEntity = new SubjectEntity("CS101", "Computer Science", "Amanda da Silva", 60L);
        
        when(subjectRepository.findByIdOptional("CS101")).thenReturn(Optional.of(existingEntity));
        doNothing().when(subjectRepository).persist(any(SubjectEntity.class));

        // When
        Subject obtained = subjectService.update(subjectToUpdate);

        // Then
        assertNotNull(obtained);
        assertEquals("CS101", obtained.getCode());
        assertEquals("Updated Computer Science", obtained.getName());
        assertEquals("João Pereira", obtained.getInstructorName());
        assertEquals(80L, obtained.getWorkload());
        verify(subjectRepository, times(1)).findByIdOptional("CS101");
        verify(subjectRepository, times(1)).persist(any(SubjectEntity.class));
    }

    @Test
    void whenUpdate_ThenFailure_SubjectNotFound() {
        // Given
        Subject subjectToUpdate = new Subject("UNKNOWN_CODE", "Updated Subject", "Luciana Souza", 80L);
        when(subjectRepository.findByIdOptional("UNKNOWN_CODE")).thenReturn(Optional.empty());

        // When & Then
        SubjectNotFoundException exception = assertThrows(SubjectNotFoundException.class, () -> {
            subjectService.update(subjectToUpdate);
        });

        assertEquals("Subject not found for code: UNKNOWN_CODE", exception.getMessage());
        verify(subjectRepository, times(1)).findByIdOptional("UNKNOWN_CODE");
        verify(subjectRepository, never()).persist(any(SubjectEntity.class));
    }

    @Test
    void whenUpdate_ThenFailure_NullSubject() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            subjectService.update(null);
        });
        verify(subjectRepository, never()).findByIdOptional(any());
        verify(subjectRepository, never()).persist(any(SubjectEntity.class));
    }

    @Test
    void testFindAll_ThenSuccess() {
        // Given
        SubjectEntity entity1 = new SubjectEntity("CS101", "Computer Science", "Amanda da Silva", 60L);
        SubjectEntity entity2 = new SubjectEntity("MATH101", "Mathematics", "Luciana Souza", 40L);
        List<SubjectEntity> entities = Arrays.asList(entity1, entity2);
        
        when(subjectRepository.findAll()).thenReturn(panacheQuery);
        when(panacheQuery.stream()).thenReturn(entities.stream());

        // When
        List<Subject> obtained = subjectService.findAll();

        // Then
        assertNotNull(obtained);
        assertEquals(2, obtained.size());
        
        assertEquals("CS101", obtained.get(0).getCode());
        assertEquals("Computer Science", obtained.get(0).getName());
        assertEquals("Amanda da Silva", obtained.get(0).getInstructorName());
        assertEquals(60L, obtained.get(0).getWorkload());
        
        assertEquals("MATH101", obtained.get(1).getCode());
        assertEquals("Mathematics", obtained.get(1).getName());
        assertEquals("Luciana Souza", obtained.get(1).getInstructorName());
        assertEquals(40L, obtained.get(1).getWorkload());
        
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(subjectRepository.findAll()).thenReturn(panacheQuery);
        when(panacheQuery.stream()).thenReturn(List.<SubjectEntity>of().stream());

        // When
        List<Subject> obtained = subjectService.findAll();

        // Then
        assertNotNull(obtained);
        assertTrue(obtained.isEmpty());
        verify(subjectRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_SingleSubject() {
        // Given
        List<SubjectEntity> entities = Arrays.asList(expectedSubjectEntity);
        when(subjectRepository.findAll()).thenReturn(panacheQuery);
        when(panacheQuery.stream()).thenReturn(entities.stream());

        // When
        List<Subject> result = subjectService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedSubject.getCode(), result.get(0).getCode());
        assertEquals(expectedSubject.getName(), result.get(0).getName());
        assertEquals(expectedSubject.getInstructorName(), result.get(0).getInstructorName());
        assertEquals(expectedSubject.getWorkload(), result.get(0).getWorkload());
        verify(subjectRepository, times(1)).findAll();
    }
}
