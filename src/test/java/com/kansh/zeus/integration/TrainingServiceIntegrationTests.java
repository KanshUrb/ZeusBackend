package com.kansh.zeus.integration;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.domain.dto.trainings.TrainingItemDto;
import com.kansh.zeus.domain.dto.trainings.TrainingSummaryDto;
import com.kansh.zeus.domain.dto.trainings.TrainingsDto;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repository.exercises.ExerciseRepository;
import com.kansh.zeus.repository.trainings.TrainingRepository;
import com.kansh.zeus.repository.users.UserRepository;
import com.kansh.zeus.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TrainingServiceIntegrationTests {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TrainingServiceImpl trainingService;

    private UsersEntity user1;
    private UsersEntity user2;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        user1 = TestDataUtil.createTestUserEntityA();
        user2 = TestDataUtil.createTestUserEntityB();
        userRepository.saveAll(Arrays.asList(user1, user2));

        ExercisesEntity testExercise1 = TestDataUtil.createTestExerciseEntity1();
        ExercisesEntity testExercise2 = TestDataUtil.createTestExerciseEntity2();
        ExercisesEntity testExercise3 = TestDataUtil.createTestExerciseEntity3();

        exerciseRepository.save(testExercise1);
        exerciseRepository.save(testExercise2);
        exerciseRepository.save(testExercise3);
    }

    @Test
    public void testSystemAndUserTrainingsSharing() {

        TrainingsEntity systemTraining = TestDataUtil.createTestTrainingEntity1();
        trainingRepository.save(systemTraining);

        TrainingsEntity userTraining = TestDataUtil.createTestUserTrainingEntity1();
        trainingRepository.save(userTraining);
        trainingService.createTraining(user1, "testName", "testNote", List.of(), List.of(user2.getHash()));

        List<TrainingSummaryDto> user1Trainings = trainingService.getTrainingsSummariesAvailableForUser(user1.getId());
        assertNotNull(user1Trainings);
        assertEquals(3, user1Trainings.size());

        List<TrainingSummaryDto> user2Trainings = trainingService.getTrainingsSummariesAvailableForUser(user2.getId());
        assertNotNull(user2Trainings);
        assertEquals(2, user2Trainings.size());
    }

    @Test
    public void testCreateAndShareTraining() {

        TrainingsEntity training = TestDataUtil.createUserTrainingEntity1();

        List<TrainingItemDto> trainingItems = new ArrayList<>();
        trainingItems.add(TestDataUtil.createTestTrainingItemDto1());
        trainingItems.add(TestDataUtil.createTestTrainingItemDto2());

        List<String> sharedWith = List.of(user2.getHash());

        TrainingsDto savedTraining = trainingService.createTraining(user1, training.getName(), training.getNote(), trainingItems, sharedWith);

        assertNotNull(savedTraining);
        assertNotNull(savedTraining.getId());

        Optional<TrainingsEntity> userTraining = trainingRepository.findTrainingByUserAndId(user2.getId(), savedTraining.getId());
        assertTrue(userTraining.isPresent());
    }

    @Test
    public void testGetTraining() {

        TrainingsEntity training = TestDataUtil.createTestUserTrainingEntity1();

        List<TrainingItemDto> trainingItems = new ArrayList<>();
        trainingItems.add(TestDataUtil.createTestTrainingItemDto1());
        trainingItems.add(TestDataUtil.createTestTrainingItemDto2());

        List<String> sharedWith = List.of(user2.getHash());

        TrainingsDto savedTraining = trainingService.createTraining(user1, training.getName(), training.getNote(), trainingItems, sharedWith);

        assertNotNull(savedTraining);
        assertNotNull(savedTraining.getId());

        Optional<TrainingsDto> trainingById = trainingService.getTrainingById(user1.getId(), savedTraining.getId());

        assertNotNull(trainingById);
        assertTrue(trainingById.isPresent());
        assertEquals(savedTraining.getId(), trainingById.get().getId());
    }
}
