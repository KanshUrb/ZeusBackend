package com.kansh.zeus.repositories;

import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.domain.entities.exercises.UserExercisesEntity;
import com.kansh.zeus.repositories.exercises.ExerciseRepository;
import com.kansh.zeus.repositories.exercises.UserExerciseRepository;
import com.kansh.zeus.repositories.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ExercisesEntityUnitTests {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserExerciseRepository userExerciseRepository;

    @Test
    void testFindAllExerciseSummariesAvailableForUser() {
        // given
        UsersEntity user = new UsersEntity();
        user.setId("user123");
        userRepository.save(user);

        ExercisesEntity exercise = new ExercisesEntity();
        exercise.setName("Push-up");
        exercise.setRate(5.0F);
        exercise.setCreatedBy(user);
        exerciseRepository.save(exercise);

        UserExercisesEntity userExercise = new UserExercisesEntity();
        userExercise.setUser(user);
        userExercise.setExercise(exercise);
        userExerciseRepository.save(userExercise);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Object[]> result = exerciseRepository.findAllExerciseSummariesAvailableForUser("user123", pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        Object[] exerciseSummary = result.getContent().get(0);
        assertThat(exerciseSummary[1]).isEqualTo("Push-up");
        assertThat(exerciseSummary[2]).isEqualTo(5.0F);
    }

    @Test
    void testFindExerciseByUserAndId() {
        // given
        UsersEntity user = new UsersEntity();
        user.setId("user123");
        userRepository.save(user);

        ExercisesEntity exercise = new ExercisesEntity();
        exercise.setName("Push-up");
        exercise.setRate(5.0F);
        exercise.setCreatedBy(user);
        exerciseRepository.save(exercise);

        UserExercisesEntity userExercise = new UserExercisesEntity();
        userExercise.setUser(user);
        userExercise.setExercise(exercise);
        userExerciseRepository.save(userExercise);

        // when
        Optional<ExercisesEntity> result = exerciseRepository.findExerciseByUserAndId("user123", exercise.getId());
        log.info(result.toString());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Push-up");
    }

    @Test
    @Transactional
    void testDeleteByUserAndId() {
        // given
        UsersEntity user = new UsersEntity();
        user.setId("user123");
        userRepository.save(user);

        ExercisesEntity exercise = new ExercisesEntity();
        exercise.setName("Push-up");
        exercise.setRate(5.0F);
        exercise.setCreatedBy(user);
        exerciseRepository.save(exercise);

        // when
        log.info("KURWA");
        log.info(String.valueOf(exercise.getId()));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Object[]> result1 = exerciseRepository.findAllExerciseSummariesAvailableForUser("user123", pageable);

        // then
        log.info(String.valueOf(result1.getTotalElements()));

        exerciseRepository.deleteByIdAndCreatedBy_Id(exercise.getId(), "user123");

        // then
        Optional<ExercisesEntity> result = exerciseRepository.findById(exercise.getId());
        assertThat(result).isNotPresent();
    }

    @Test
    void testUser2AccessesUser1SharedExercise() {
        // given
        UsersEntity user1 = new UsersEntity();
        user1.setId("user1");
        userRepository.save(user1);

        UsersEntity user2 = new UsersEntity();
        user2.setId("user2");
        userRepository.save(user2);

        ExercisesEntity exercise = new ExercisesEntity();
        exercise.setName("Push-up");
        exercise.setRate(5.0F);
        exercise.setCreatedBy(user1);
        exerciseRepository.save(exercise);

        UserExercisesEntity userExercise = new UserExercisesEntity();
        userExercise.setUser(user1);
        userExercise.setExercise(exercise);
        userExerciseRepository.save(userExercise);

        UserExercisesEntity sharedExercise = new UserExercisesEntity();
        sharedExercise.setUser(user1); // user1 jako autor
        sharedExercise.setExercise(exercise);
        sharedExercise.setSharedWith(user2); // user1 udostępnia ćwiczenie user2
        userExerciseRepository.save(sharedExercise);

        // Save all changes to the database
        userRepository.flush();
        exerciseRepository.flush();
        userExerciseRepository.flush();

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Object[]> result = exerciseRepository.findAllExerciseSummariesAvailableForUser("user2", pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        Object[] exerciseSummary = result.getContent().get(0);
        assertThat(exerciseSummary[1]).isEqualTo("Push-up");
        assertThat(exerciseSummary[2]).isEqualTo(5.0F);
    }
}
