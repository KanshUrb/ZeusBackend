package com.kansh.zeus.controllers;

import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repositories.exercises.ExerciseRepository;
import com.kansh.zeus.repositories.exercises.SupersetRepository;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.impl.ExerciseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class ExerciseServiceIntegrationTests {

    @Autowired
    private ExerciseServiceImpl exercisesService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private SupersetRepository supersetRepository;

    @Autowired
    private UserRepository userRepository;

    private UsersEntity user1;
    private UsersEntity user2;
    private ExercisesEntity exercise1;
    private ExercisesEntity exercise2;
    private ExercisesEntity exercise3;
    private SupersetsEntity superset1;
    private SupersetsEntity superset2;
    private SupersetsEntity superset3;

    @BeforeEach
    public void setUp() {
        // Create users
        user1 = UsersEntity.builder()
                .id("user1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        user2 = UsersEntity.builder()
                .id("user2")
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        // Save users to repository
        userRepository.saveAll(Arrays.asList(user1, user2));

        // Create exercises
        exercise1 = ExercisesEntity.builder()
                .name("Push-up")
                .description("Push-up description")
                .muscleGroup("Chest")
                .difficultyLevel(3)
                .videoUrl("http://video.url")
                .build();

        exercise2 = ExercisesEntity.builder()
                .name("Squat")
                .description("Squat description")
                .muscleGroup("Legs")
                .difficultyLevel(4)
                .videoUrl("http://video.url/squat")
                .build();

        exercise3 = ExercisesEntity.builder()
                .name("Pull-up")
                .description("Pull-up description")
                .muscleGroup("Back")
                .difficultyLevel(5)
                .videoUrl("http://video.url/pullup")
                .build();

        // Save exercises to repository
        exerciseRepository.saveAll(Arrays.asList(exercise1, exercise2, exercise3));

        // Create common supersets
        superset1 = SupersetsEntity.builder()
                .name("Superset 1")
                .exercise1(exercise1)
                .exercise2(exercise2)
                .build();

        superset2 = SupersetsEntity.builder()
                .name("Superset 2")
                .exercise1(exercise2)
                .exercise2(exercise3)
                .build();

        superset3 = SupersetsEntity.builder()
                .name("Superset 3")
                .exercise1(exercise1)
                .exercise2(exercise3)
                .build();

        // Save supersets to repository
        supersetRepository.saveAll(Arrays.asList(superset1, superset2, superset3));
    }

    @Test
    public void testComprehensiveExerciseScenario() {
        // Scenario: Check if common exercises are visible to both users
        Page<Object[]> resultUser1 = exercisesService.getExercisesSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        Page<Object[]> resultUser2 = exercisesService.getExercisesSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));

        assertEquals(3, resultUser1.getTotalElements());
        assertEquals(3, resultUser2.getTotalElements());

        // Scenario: User1 creates an exercise visible only to him
        ExercisesEntity userExercise1 = ExercisesEntity.builder()
                .name("User1's Exercise")
                .description("User1 only")
                .createdBy(user1)
                .build();

        ExercisesEntity createdExercise1 = exercisesService.createExercise(userExercise1, user1, Collections.emptyList());
        assertNotNull(createdExercise1);

        ExercisesEntity exercise = ExercisesEntity.builder()
                .id(0L)
                .name("Exercise")
                .build();
        ExercisesEntity createdExercise = exercisesService.createExercise(exercise, user1, Collections.emptyList());
        log.info("KURWA");
        log.info(createdExercise.toString());

        resultUser1 = exercisesService.getExercisesSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        assertEquals(4, resultUser1.getTotalElements());

        resultUser2 = exercisesService.getExercisesSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));
        assertEquals(3, resultUser2.getTotalElements());

        // Scenario: User1 creates an exercise and shares it with User2
        ExercisesEntity userExercise2 = ExercisesEntity.builder()
                .name("Shared Exercise")
                .description("Shared with User2")
                .createdBy(user1)
                .build();

        ExercisesEntity createdExercise2 = exercisesService.createExercise(userExercise2, user1, List.of(user2.getId()));
        assertNotNull(createdExercise2);

        resultUser1 = exercisesService.getExercisesSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        assertEquals(5, resultUser1.getTotalElements());

        resultUser2 = exercisesService.getExercisesSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));
        assertEquals(4, resultUser2.getTotalElements());
    }

    @Test
    public void testComprehensiveSupersetScenario() {
        // Scenario: Check if common supersets are visible to both users
        Page<Object[]> resultUser1 = exercisesService.getSupersetsSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        Page<Object[]> resultUser2 = exercisesService.getSupersetsSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));

        assertEquals(3, resultUser1.getTotalElements());
        assertEquals(3, resultUser2.getTotalElements());

        // Scenario: User1 creates a superset visible only to him
        SupersetsEntity userSuperset1 = SupersetsEntity.builder()
                .id(0L)
                .name("User1's Superset")
                .exercise1(exercise1)
                .exercise2(exercise2)
                .createdBy(user1)
                .build();

        SupersetsEntity createdSuperset1 = exercisesService.createSuperset(userSuperset1, user1, Collections.emptyList());
        log.info("KURWA");
        log.info(createdSuperset1.toString());
        assertNotNull(createdSuperset1);

        resultUser1 = exercisesService.getSupersetsSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        assertEquals(4, resultUser1.getTotalElements());

        resultUser2 = exercisesService.getSupersetsSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));
        assertEquals(3, resultUser2.getTotalElements());

        // Scenario: User1 creates a superset and shares it with User2
        SupersetsEntity userSuperset2 = SupersetsEntity.builder()
                .name("Shared Superset")
                .exercise1(exercise2)
                .exercise2(exercise3)
                .createdBy(user1)
                .build();

        SupersetsEntity createdSuperset2 = exercisesService.createSuperset(userSuperset2, user1, List.of(user2.getId()));
        assertNotNull(createdSuperset2);

        resultUser1 = exercisesService.getSupersetsSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        assertEquals(5, resultUser1.getTotalElements());

        resultUser2 = exercisesService.getSupersetsSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));
        assertEquals(4, resultUser2.getTotalElements());
    }

    @Test
    public void testGetExerciseByUserAndId() {
        // Assign user to exercise1
        exercise1.setCreatedBy(user1);
        exerciseRepository.save(exercise1);

        // Retrieve exercise for user1
        Optional<ExercisesEntity> retrievedExercise1 = exercisesService.getExerciseByUserAndId(user1.getId(), exercise1.getId());
        assertTrue(retrievedExercise1.isPresent());
        assertEquals(exercise1.getName(), retrievedExercise1.get().getName());

        // Retrieve exercise for user2 (should return empty if not shared)
        Optional<ExercisesEntity> retrievedExercise2 = exercisesService.getExerciseByUserAndId(user2.getId(), exercise1.getId());
        assertFalse(retrievedExercise2.isPresent());
    }

    @Test
    public void testGetSupersetByUserAndId() {
        // Assign user to superset1
        superset1.setCreatedBy(user1);
        supersetRepository.save(superset1);

        // Retrieve superset for user1
        Optional<SupersetsEntity> retrievedSuperset1 = exercisesService.getSupersetByUserAndId(user1.getId(), superset1.getId());
        assertTrue(retrievedSuperset1.isPresent());
        assertEquals(superset1.getName(), retrievedSuperset1.get().getName());

        // Retrieve superset for user2 (should return empty if not shared)
        Optional<SupersetsEntity> retrievedSuperset2 = exercisesService.getSupersetByUserAndId(user2.getId(), superset1.getId());
        assertFalse(retrievedSuperset2.isPresent());
    }

    @Test
    public void testRateExercise() {

        exercisesService.rateExercise(exercise1.getId(), 1);
        exercisesService.rateExercise(exercise1.getId(), 4);
        exercisesService.rateExercise(exercise1.getId(), 5);

        Optional<ExercisesEntity> retrievedExercise = exercisesService.getExerciseByUserAndId(user1.getId(), exercise1.getId());
        assertTrue(retrievedExercise.isPresent());
        assertEquals(3.3333333F, retrievedExercise.get().getRate());
    }

    @Test
    public void testRateSuperset() {

        exercisesService.rateSuperset(superset1.getId(), 1);
        exercisesService.rateSuperset(superset1.getId(), 4);
        exercisesService.rateSuperset(superset1.getId(), 5);

        Optional<SupersetsEntity> retrievedSuperset = exercisesService.getSupersetByUserAndId(user1.getId(), exercise1.getId());
        log.info(retrievedSuperset.toString());
        assertTrue(retrievedSuperset.isPresent());
        assertEquals(3.3333333F, retrievedSuperset.get().getRate());
    }

}
