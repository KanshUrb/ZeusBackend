package com.kansh.zeus.repositories.exercises;

import com.kansh.zeus.domain.entities.exercises.UserExercisesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExerciseRepository extends JpaRepository<UserExercisesEntity, Long> {
}
