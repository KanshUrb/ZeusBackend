package com.kansh.zeus.unit;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.domain.dto.exercises.ExerciseDetailsDto;
import com.kansh.zeus.domain.dto.exercises.SupersetWrapperDto;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.exercises.UserExercisesEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repository.exercises.ExerciseRepository;
import com.kansh.zeus.repository.exercises.SupersetRepository;
import com.kansh.zeus.repository.exercises.UserExerciseRepository;
import com.kansh.zeus.repository.exercises.UserSupersetRepository;
import com.kansh.zeus.repository.users.UserRepository;
import com.kansh.zeus.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ExerciseServiceUnitTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserExerciseRepository userExerciseRepository;

    @Mock
    private SupersetRepository supersetRepository;

    @Mock
    private UserSupersetRepository userSupersetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateExercise() {
        UsersEntity user = TestDataUtil.createTestUserEntityA();
        ExercisesEntity exercise = TestDataUtil.createTestExerciseEntity1();

        when(exerciseRepository.save(any(ExercisesEntity.class))).thenReturn(exercise);
        when(userRepository.findByHash(anyString())).thenReturn(Optional.of(TestDataUtil.createTestUserEntityB()));

        ExercisesEntity result = exerciseService.createExercise(exercise, user, List.of(TestDataUtil.HASH_B));

        assertNotNull(result);
        assertEquals(TestDataUtil.EXERCISE_NAME_1, result.getName());
        verify(exerciseRepository, times(1)).save(exercise);
        verify(userExerciseRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testDeleteExercise() {
        doNothing().when(exerciseRepository).deleteByIdAndCreatedBy_Id(anyLong(), anyString());

        exerciseService.deleteExercise(TestDataUtil.EXERCISE_ID_1, TestDataUtil.ID_A);

        verify(exerciseRepository, times(1)).deleteByIdAndCreatedBy_Id(TestDataUtil.EXERCISE_ID_1, TestDataUtil.ID_A);
    }

    @Test
    public void testUpdateExercise() {

        ExercisesEntity existingExercise = TestDataUtil.createTestExerciseEntity1();

        ExerciseDetailsDto exerciseDetails = TestDataUtil.createExerciseDetailsDtoB();

        when(exerciseRepository.findById(TestDataUtil.EXERCISE_ID_2)).thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.save(any(ExercisesEntity.class))).thenReturn(existingExercise);
        when(userRepository.findByHash(anyString())).thenReturn(Optional.of(TestDataUtil.createTestUserEntityB()));

        ExerciseDetailsDto updatedExercise = exerciseService.updateExercise(TestDataUtil.ID_A, existingExercise, exerciseDetails);

        assertEquals(TestDataUtil.EXERCISE_NAME_2, updatedExercise.getExercise().getName());
        verify(exerciseRepository, times(1)).save(existingExercise);
    }

    @Test
    public void testRateExercise() {
        ExercisesEntity exercise = TestDataUtil.createTestExerciseEntity1();
        exercise.setRate(4.0f);
        exercise.setUserCounter(1);

        when(exerciseRepository.findById(TestDataUtil.EXERCISE_ID_1)).thenReturn(Optional.of(exercise));
        when(exerciseRepository.save(any(ExercisesEntity.class))).thenReturn(exercise);

        Float newRate = exerciseService.rateExercise(TestDataUtil.EXERCISE_ID_1, 5);

        assertEquals(4.5f, newRate);
        verify(exerciseRepository, times(1)).save(exercise);
    }

    @Test
    public void testGetExerciseByUserAndId() {
        ExercisesEntity exercise = TestDataUtil.createTestExerciseEntity1();
        when(exerciseRepository.findExerciseByUserAndId(TestDataUtil.ID_A, TestDataUtil.EXERCISE_ID_1))
                .thenReturn(Optional.of(exercise));

        Optional<ExercisesEntity> result = exerciseService.getExerciseByUserAndId(TestDataUtil.ID_A, TestDataUtil.EXERCISE_ID_1);

        assertTrue(result.isPresent());
        assertEquals(exercise, result.get());
        verify(exerciseRepository, times(1)).findExerciseByUserAndId(TestDataUtil.ID_A, TestDataUtil.EXERCISE_ID_1);
    }

    @Test
    public void testGetExercisesSummariesAvailableForUser() {
        PageRequest pageable = PageRequest.of(0, 10);
        when(exerciseRepository.findAllExerciseSummariesAvailableForUser(TestDataUtil.ID_A, pageable))
                .thenReturn(Page.empty());

        Page<Object[]> result = exerciseService.getExercisesSummariesAvailableForUser(TestDataUtil.ID_A, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(exerciseRepository, times(1)).findAllExerciseSummariesAvailableForUser(TestDataUtil.ID_A, pageable);
    }

    @Test
    public void testGetSharedWithExercise() {
        UserExercisesEntity userExercise = new UserExercisesEntity();
        userExercise.setSharedWith(TestDataUtil.createTestUserEntityB());

        when(userExerciseRepository.findAllByExercise_Id(TestDataUtil.EXERCISE_ID_1))
                .thenReturn(List.of(userExercise));

        List<UsersEntity> sharedWith = exerciseService.getSharedWith(1, TestDataUtil.EXERCISE_ID_1);

        assertEquals(1, sharedWith.size());
        assertEquals(TestDataUtil.ID_B, sharedWith.get(0).getId());
    }

    @Test
    public void testGetSupersetsSummariesAvailableForUser() {
        PageRequest pageable = PageRequest.of(0, 10);
        when(supersetRepository.findAllSupersetsSummariesAvailableForUser(TestDataUtil.ID_A, pageable))
                .thenReturn(Page.empty());

        Page<Object[]> result = exerciseService.getSupersetsSummariesAvailableForUser(TestDataUtil.ID_A, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(supersetRepository, times(1)).findAllSupersetsSummariesAvailableForUser(TestDataUtil.ID_A, pageable);
    }

    @Test
    public void testGetSupersetByUserAndId() {
        SupersetsEntity superset = new SupersetsEntity();
        when(supersetRepository.findSupersetByUserAndId(TestDataUtil.ID_A, TestDataUtil.EXERCISE_ID_1))
                .thenReturn(Optional.of(superset));

        Optional<SupersetsEntity> result = exerciseService.getSupersetByUserAndId(TestDataUtil.ID_A, TestDataUtil.EXERCISE_ID_1);

        assertTrue(result.isPresent());
        assertEquals(superset, result.get());
        verify(supersetRepository, times(1)).findSupersetByUserAndId(TestDataUtil.ID_A, TestDataUtil.EXERCISE_ID_1);
    }

    @Test
    public void testRateSuperset() {
        SupersetsEntity superset = new SupersetsEntity();
        superset.setRate(4.0f);
        superset.setUserCounter(1);

        when(supersetRepository.findById(TestDataUtil.EXERCISE_ID_1)).thenReturn(Optional.of(superset));
        when(supersetRepository.save(any(SupersetsEntity.class))).thenReturn(superset);

        Float newRate = exerciseService.rateSuperset(TestDataUtil.EXERCISE_ID_1, 5);

        assertEquals(4.5f, newRate);
        verify(supersetRepository, times(1)).save(superset);
    }

    @Test
    public void testCreateSuperset() {
        UsersEntity user = TestDataUtil.createTestUserEntityA();
        SupersetsEntity superset = new SupersetsEntity();

        when(supersetRepository.save(any(SupersetsEntity.class))).thenReturn(superset);
        when(userRepository.findByHash(anyString())).thenReturn(Optional.of(TestDataUtil.createTestUserEntityB()));

        SupersetsEntity result = exerciseService.createSuperset(superset, user, List.of(TestDataUtil.HASH_B));

        assertNotNull(result);
        verify(supersetRepository, times(1)).save(superset);
        verify(userSupersetRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testDeleteSuperset() {
        doNothing().when(supersetRepository).deleteByIdAndCreatedBy_Id(anyLong(), anyString());

        exerciseService.deleteSuperset(TestDataUtil.EXERCISE_ID_1, TestDataUtil.ID_A);

        verify(supersetRepository, times(1)).deleteByIdAndCreatedBy_Id(TestDataUtil.EXERCISE_ID_1, TestDataUtil.ID_A);
    }

    @Test
    public void testUpdateSuperset() {

        SupersetsEntity existingSuperset = TestDataUtil.createSupersetEntityA();

        SupersetWrapperDto supersetWrapperDto = TestDataUtil.createSupersetWrapperDtoA();

        when(exerciseRepository.findById(TestDataUtil.EXERCISE_ID_1)).thenReturn(Optional.of(TestDataUtil.createTestExerciseEntity1()));
        when(exerciseRepository.findById(TestDataUtil.EXERCISE_ID_2)).thenReturn(Optional.of(TestDataUtil.createTestExerciseEntity2()));
        when(supersetRepository.save(any(SupersetsEntity.class))).thenReturn(existingSuperset);
        when(userRepository.findByHash(anyString())).thenReturn(Optional.of(TestDataUtil.createTestUserEntityB()));

        SupersetWrapperDto updatedSuperset = exerciseService.updateSuperset(TestDataUtil.ID_A, existingSuperset, supersetWrapperDto);

        assertEquals(TestDataUtil.SUPERSET_NAME_1, updatedSuperset.getSuperset().getName());
        verify(supersetRepository, times(1)).save(existingSuperset);
    }
}
