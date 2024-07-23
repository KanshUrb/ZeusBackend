package com.kansh.zeus.controllers;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.domain.dto.exercises.ExercisesDto;
import com.kansh.zeus.domain.dto.exercises.SupersetsDto;
import com.kansh.zeus.domain.dto.trainings.SeriesDto;
import com.kansh.zeus.domain.dto.trainings.TrainingItemDto;
import com.kansh.zeus.domain.dto.trainings.TrainingsDto;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsEntity;
import com.kansh.zeus.domain.entities.trainings.UserTrainingsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repositories.exercises.ExerciseRepository;
import com.kansh.zeus.repositories.exercises.SupersetRepository;
import com.kansh.zeus.repositories.trainings.TrainingItemRepository;
import com.kansh.zeus.repositories.trainings.TrainingRepository;
import com.kansh.zeus.repositories.trainings.UserTrainingsRepository;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.impl.TrainingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TrainingServiceIntegrationTests {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainingItemRepository trainingItemRepository;

    @Autowired
    private UserTrainingsRepository userTrainingsRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private SupersetRepository supersetRepository;

    @Autowired
    private TrainingServiceImpl trainingService;

    private UsersEntity user1;
    private UsersEntity user2;
    private ExercisesEntity testExercise1;
    private ExercisesEntity testExercise2;
    private ExercisesEntity testExercise3;
    private SupersetsEntity testSuperset1;
    private SupersetsEntity testSuperset2;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Initialize test data using TestDataUtil
        user1 = TestDataUtil.createTestUserEntityA();
        user2 = TestDataUtil.createTestUserEntityB();
        userRepository.saveAll(Arrays.asList(user1, user2));

        testExercise1 = TestDataUtil.createTestExerciseEntity1();
        testExercise2 = TestDataUtil.createTestExerciseEntity2();
        testExercise3 = TestDataUtil.createTestExerciseEntity3();

        testSuperset1 = SupersetsEntity.builder().id(1L).name("Superset 1").createdBy(user1).build();
        testSuperset2 = SupersetsEntity.builder().id(2L).name("Superset 2").createdBy(user1).build();

        exerciseRepository.save(testExercise1);
        exerciseRepository.save(testExercise2);
        exerciseRepository.save(testExercise3);

        supersetRepository.save(testSuperset1);
        supersetRepository.save(testSuperset2);
    }

    @Test
    public void testSystemAndUserTrainingsSharing() {
        // Create system trainings
        TrainingsEntity systemTraining1 = new TrainingsEntity();
        systemTraining1.setName("System Training 1");
        systemTraining1.setCreatedBy(null);
        trainingRepository.save(systemTraining1);

        TrainingsEntity systemTraining2 = new TrainingsEntity();
        systemTraining2.setName("System Training 2");
        systemTraining2.setCreatedBy(null);
        trainingRepository.save(systemTraining2);

        // Create user1 trainings
        TrainingsEntity userTraining1 = new TrainingsEntity();
        userTraining1.setName("User Training 1");
        userTraining1.setCreatedBy(user1);
        trainingRepository.save(userTraining1);

        TrainingsEntity userTraining2 = new TrainingsEntity();
        userTraining2.setName("User Training 2");
        userTraining2.setCreatedBy(user1);
        trainingRepository.save(userTraining2);

        // Share one training from user1 to user2
        userTrainingsRepository.save(UserTrainingsEntity.builder()
                .user(user1)
                .training(userTraining1)
                .sharedWith(user2)
                .build());

        // Test for user1
        Pageable pageable = PageRequest.of(0, 10);
        Page<Object[]> user1Trainings = trainingService.getTrainingsSummariesAvailableForUser(user1.getId(), pageable);
        assertNotNull(user1Trainings);
        assertEquals(4, user1Trainings.getTotalElements());

        // Test for user2
        Page<Object[]> user2Trainings = trainingService.getTrainingsSummariesAvailableForUser(user2.getId(), pageable);
        assertNotNull(user2Trainings);
        assertEquals(3, user2Trainings.getTotalElements());

    }

    @Test
    public void testCreateAndShareTraining() {

        TrainingsEntity training = new TrainingsEntity();
        training.setName("Test Training");
        training.setNote("Example note");
        training.setCreatedBy(user1);

        List<SeriesDto> series1 = new ArrayList<>();
        SeriesDto s1 = SeriesDto.builder()
                .repetitions(10)
                .weight1(20.0F)
                .build();
        series1.add(s1);
        SeriesDto s2 = SeriesDto.builder()
                .repetitions(8)
                .weight1(20.0F)
                .build();
        series1.add(s2);
        List<SeriesDto> series2 = new ArrayList<>();
        SeriesDto s3 = SeriesDto.builder()
                .repetitions(10)
                .weight1(20.0F)
                .weight2(15.0F)
                .build();
        series2.add(s3);
        SeriesDto s4 = SeriesDto.builder()
                .repetitions(10)
                .weight1(15.0F)
                .weight2(10.5F)
                .build();
        series2.add(s4);


        List<TrainingItemDto> trainingItems = new ArrayList<>();
        ExercisesDto testExerciseDto1 = ExercisesDto.builder()
                .id(testExercise1.getId())
                .name(testExercise1.getName())
                .description(testExercise1.getDescription())
                .muscleGroup(testExercise1.getMuscleGroup())
                .videoUrl(testExercise1.getVideoUrl())
                .rate(testExercise1.getRate())
                .build();
        trainingItems.add(TrainingItemDto.builder()
                .itemType(1)
                .exercise(testExerciseDto1)
                .series(series1)
                .build());

        SupersetsDto testSupersetDto1 = SupersetsDto.builder()
                .id(testSuperset1.getId())
                .name(testSuperset1.getName())
                .exercise1(testExercise1.getId())
                .exercise2(testExercise2.getId())
                .rate(testSuperset1.getRate())
                .build();
        trainingItems.add(TrainingItemDto.builder()
                .itemType(2)
                .superset(testSupersetDto1)
                .series(series2)
                .build());

        List<String> sharedWith = List.of(user2.getId());

        TrainingsDto savedTraining = trainingService.createTraining(user1, training.getName(), training.getNote(), trainingItems, sharedWith);

        log.info(savedTraining.toString());

        assertNotNull(savedTraining);
        /*assertNotNull(savedTraining.getId());

        List<TrainingsItemsEntity> trainingItems = trainingItemRepository.findByTrainingId(savedTraining.getId());
        assertFalse(trainingItems.isEmpty());
        assertEquals(5, trainingItems.size());

        long exerciseCount = trainingItems.stream().filter(item -> item.getItemType() == 1).count();
        long supersetCount = trainingItems.stream().filter(item -> item.getItemType() == 2).count();

        assertEquals(3, exerciseCount);
        assertEquals(2, supersetCount);

        Optional<TrainingsEntity> userTraining = trainingRepository.findTrainingByUserAndId(user2.getId(), savedTraining.getId());
        assertTrue(userTraining.isPresent());*/
    }

    @Disabled
    @Test
    public void testCreateTrainingWithExercisesAndSupersets() {
        // Create exercises
        ExercisesEntity exercise1 = TestDataUtil.createTestExerciseEntity1();
        ExercisesEntity exercise2 = TestDataUtil.createTestExerciseEntity2();
        ExercisesEntity exercise3 = TestDataUtil.createTestExerciseEntity3();

        exerciseRepository.save(exercise1);
        exerciseRepository.save(exercise2);
        exerciseRepository.save(exercise3);

        // Create supersets
        SupersetsEntity superset1 = SupersetsEntity.builder().name("Superset 1").createdBy(user1).build();
        SupersetsEntity superset2 = SupersetsEntity.builder().name("Superset 2").createdBy(user1).build();

        supersetRepository.save(superset1);
        supersetRepository.save(superset2);

        // Create training
        TrainingsEntity training = new TrainingsEntity();
        training.setName("Test Training");
        training.setCreatedBy(user1);

        List<Pair<Long, Integer>> items = List.of(
                Pair.of(exercise1.getId(), 1),
                Pair.of(exercise2.getId(), 1),
                Pair.of(exercise3.getId(), 1),
                Pair.of(superset1.getId(), 2),
                Pair.of(superset2.getId(), 2)
        );

        /*TrainingsEntity savedTraining = trainingService.createTraining(training, user1, items, List.of());

        assertNotNull(savedTraining);
        assertNotNull(savedTraining.getId());

        // Retrieve and verify training items
        List<TrainingsItemsEntity> trainingItems = trainingItemRepository.findByTrainingId(savedTraining.getId());
        assertNotNull(trainingItems);
        assertEquals(5, trainingItems.size());

        long exerciseCount = trainingItems.stream().filter(item -> item.getItemType() == 1).count();
        long supersetCount = trainingItems.stream().filter(item -> item.getItemType() == 2).count();

        assertEquals(3, exerciseCount);
        assertEquals(2, supersetCount);

        // Verify that the exercises and supersets are correctly associated with the training
        assertTrue(trainingItems.stream().anyMatch(item -> item.getExercise() != null && item.getExercise().getId().equals(exercise1.getId())));
        assertTrue(trainingItems.stream().anyMatch(item -> item.getExercise() != null && item.getExercise().getId().equals(exercise2.getId())));
        assertTrue(trainingItems.stream().anyMatch(item -> item.getExercise() != null && item.getExercise().getId().equals(exercise3.getId())));

        assertTrue(trainingItems.stream().anyMatch(item -> item.getSuperset() != null && item.getSuperset().getId().equals(superset1.getId())));
        assertTrue(trainingItems.stream().anyMatch(item -> item.getSuperset() != null && item.getSuperset().getId().equals(superset2.getId())));
        */
    }

}
