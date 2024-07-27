package com.kansh.zeus.repositories.exercises;

import com.kansh.zeus.domain.entities.exercises.UserExercisesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserExerciseRepository extends JpaRepository<UserExercisesEntity, Long> {
    List<UserExercisesEntity> findAllByExercise_Id(Long exerciseId);

    void deleteByExercise_IdAndSharedWith_Hash(Long id, String hash);

    @Modifying
    @Transactional
    void deleteByExercise_Id(Long exerciseId);
}
