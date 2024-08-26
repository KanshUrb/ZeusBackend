package com.kansh.zeus.unit;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.domain.dto.trainings.TrainingsDto;
import com.kansh.zeus.domain.dto.trainings.TrainingSummaryDto;
import com.kansh.zeus.domain.entities.trainings.TrainingsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repository.exercises.ExerciseRepository;
import com.kansh.zeus.repository.trainings.TrainingRepository;
import com.kansh.zeus.repository.users.UserRepository;
import com.kansh.zeus.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainingServiceUnitTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTrainingsSummariesAvailableForUser() {
        when(trainingRepository.findAllTrainingSummariesAvailableForUser(TestDataUtil.ID_A))
                .thenReturn(List.of(TestDataUtil.createTestTrainingEntity1()));

        List<TrainingSummaryDto> summaries = trainingService.getTrainingsSummariesAvailableForUser(TestDataUtil.ID_A);

        assertNotNull(summaries);
        assertEquals(1, summaries.size());
        assertEquals(TestDataUtil.TRAINING_NAME_1, summaries.get(0).getName());
    }

    @Test
    public void testGetTrainingById() {
        when(trainingRepository.findTrainingByUserAndId(TestDataUtil.ID_A, TestDataUtil.TRAINING_ID_1))
                .thenReturn(Optional.of(TestDataUtil.createTestTrainingEntity1()));
        when(trainingRepository.findTrainingItemsByTrainingId(TestDataUtil.TRAINING_ID_1))
                .thenReturn(List.of(TestDataUtil.createTestTrainingItemEntity1()));
        when(trainingRepository.findTrainingItemsSeriesByTrainingItemsId(TestDataUtil.TRAINING_ITEM_ID_1))
                .thenReturn(List.of(TestDataUtil.createTestTrainingItemSeriesEntity1()));

        Optional<TrainingsDto> result = trainingService.getTrainingById(TestDataUtil.ID_A, TestDataUtil.TRAINING_ID_1);

        assertTrue(result.isPresent());
        assertEquals(TestDataUtil.TRAINING_NAME_1, result.get().getName());
    }

    @Test
    public void testCreateTraining() {
        UsersEntity user = TestDataUtil.createTestUserEntityA();
        TrainingsEntity savedTraining = TestDataUtil.createTestTrainingEntity1();
        when(trainingRepository.save(any(TrainingsEntity.class))).thenReturn(savedTraining);
        when(userRepository.findByHash(TestDataUtil.HASH_B)).thenReturn(Optional.of(TestDataUtil.createTestUserEntityB()));
        when(exerciseRepository.findExerciseByUserAndId(TestDataUtil.ID_A, TestDataUtil.EXERCISE_ID_1))
                .thenReturn(Optional.of(TestDataUtil.createTestExerciseEntity1()));

        TrainingsDto result = trainingService.createTraining(user, TestDataUtil.TRAINING_NAME_1, TestDataUtil.TRAINING_NOTE_1,
                List.of(TestDataUtil.createTestTrainingItemDto1()), Collections.emptyList());

        assertNotNull(result);
        assertEquals(TestDataUtil.TRAINING_NAME_1, result.getName());
        verify(trainingRepository, times(1)).save(any(TrainingsEntity.class));
    }

    @Test
    public void testDeleteTraining() {
        doNothing().when(trainingRepository).deleteByIdAndCreatedBy_Id(TestDataUtil.TRAINING_ID_1, TestDataUtil.ID_A);

        trainingService.deleteTraining(TestDataUtil.TRAINING_ID_1, TestDataUtil.ID_A);

        verify(trainingRepository, times(1)).deleteByIdAndCreatedBy_Id(TestDataUtil.TRAINING_ID_1, TestDataUtil.ID_A);
    }
}
