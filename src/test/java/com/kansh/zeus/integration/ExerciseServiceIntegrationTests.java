package com.kansh.zeus.integration;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repository.exercises.ExerciseRepository;
import com.kansh.zeus.repository.exercises.SupersetRepository;
import com.kansh.zeus.repository.users.UserRepository;
import com.kansh.zeus.service.impl.ExerciseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
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
    private SupersetsEntity superset1;

    @BeforeEach
    public void setUp() throws SQLException {

        user1 = TestDataUtil.createTestUserEntityA();
        user2 = TestDataUtil.createTestUserEntityB();
        exercise1 = TestDataUtil.createTestExerciseEntity1();
        ExercisesEntity exercise2 = TestDataUtil.createTestExerciseEntity2();
        ExercisesEntity exercise3 = TestDataUtil.createTestExerciseEntity3();
        superset1 = TestDataUtil.createSupersetEntityA();
        SupersetsEntity superset2 = TestDataUtil.createSupersetEntityB();

        userRepository.saveAll(List.of(user1, user2));
        exerciseRepository.saveAll(List.of(exercise1, exercise2, exercise3));
        supersetRepository.saveAll(List.of(superset1, superset2));
    }

    @Test
    public void testComprehensiveExerciseScenario() {

        Page<Object[]> resultUser1 = exercisesService.getExercisesSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        Page<Object[]> resultUser2 = exercisesService.getExercisesSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));

        assertEquals(3, resultUser1.getTotalElements());
        assertEquals(3, resultUser2.getTotalElements());

        ExercisesEntity userExercise1 = TestDataUtil.createTestUserExerciseEntity1();

        ExercisesEntity createdExercise1 = exercisesService.createExercise(userExercise1, user1, Collections.emptyList());
        assertNotNull(createdExercise1);

        resultUser1 = exercisesService.getExercisesSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        assertEquals(4, resultUser1.getTotalElements());

        resultUser2 = exercisesService.getExercisesSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));
        assertEquals(3, resultUser2.getTotalElements());

        ExercisesEntity userExercise2 = TestDataUtil.createTestUserExerciseEntity2();

        ExercisesEntity createdExercise2 = exercisesService.createExercise(userExercise2, user1, List.of(user2.getHash()));
        assertNotNull(createdExercise2);

        resultUser1 = exercisesService.getExercisesSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        assertEquals(5, resultUser1.getTotalElements());

        resultUser2 = exercisesService.getExercisesSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));
        assertEquals(4, resultUser2.getTotalElements());
    }

    @Test
    public void testComprehensiveSupersetScenario() {

        Page<Object[]> resultUser1 = exercisesService.getSupersetsSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        Page<Object[]> resultUser2 = exercisesService.getSupersetsSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));

        assertEquals(2, resultUser1.getTotalElements());
        assertEquals(2, resultUser2.getTotalElements());

        SupersetsEntity userSuperset1 = TestDataUtil.createUserSupersetEntityA();

        SupersetsEntity createdSuperset1 = exercisesService.createSuperset(userSuperset1, user1, Collections.emptyList());
        assertNotNull(createdSuperset1);

        resultUser1 = exercisesService.getSupersetsSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        assertEquals(3, resultUser1.getTotalElements());

        resultUser2 = exercisesService.getSupersetsSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));
        assertEquals(2, resultUser2.getTotalElements());

        SupersetsEntity userSuperset2 = TestDataUtil.createUserSupersetEntityB();

        SupersetsEntity createdSuperset2 = exercisesService.createSuperset(userSuperset2, user1, List.of(user2.getHash()));
        assertNotNull(createdSuperset2);

        resultUser1 = exercisesService.getSupersetsSummariesAvailableForUser(user1.getId(), PageRequest.of(0, 10));
        assertEquals(4, resultUser1.getTotalElements());

        resultUser2 = exercisesService.getSupersetsSummariesAvailableForUser(user2.getId(), PageRequest.of(0, 10));
        assertEquals(3, resultUser2.getTotalElements());
    }

    @Test
    public void testGetExerciseByUserAndId() {

        exercise1.setCreatedBy(user1);
        exerciseRepository.save(exercise1);

        Optional<ExercisesEntity> retrievedExercise1 = exercisesService.getExerciseByUserAndId(user1.getId(), exercise1.getId());
        assertTrue(retrievedExercise1.isPresent());
        assertEquals(exercise1.getName(), retrievedExercise1.get().getName());

        Optional<ExercisesEntity> retrievedExercise2 = exercisesService.getExerciseByUserAndId(user2.getId(), exercise1.getId());
        assertFalse(retrievedExercise2.isPresent());
    }

    @Test
    public void testGetSupersetByUserAndId() {

        superset1.setCreatedBy(user1);
        supersetRepository.save(superset1);

        Optional<SupersetsEntity> retrievedSuperset1 = exercisesService.getSupersetByUserAndId(user1.getId(), superset1.getId());
        assertTrue(retrievedSuperset1.isPresent());
        assertEquals(superset1.getName(), retrievedSuperset1.get().getName());

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

        Optional<SupersetsEntity> retrievedSuperset = exercisesService.getSupersetByUserAndId(user1.getId(), superset1.getId());
        assertTrue(retrievedSuperset.isPresent());
        assertEquals(3.3333333F, retrievedSuperset.get().getRate());
    }

}
