package com.kansh.zeus.repository.exercises;

import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<ExercisesEntity, Long> {

    @Query(value = "SELECT "
                 + "  e.id, "
                 + "  e.name, "
                 + "  e.rate "
                 + "FROM ExercisesEntity e "
                 + "LEFT JOIN UserExercisesEntity ue ON e.id = ue.exercise.id "
                 + "WHERE e.createdBy IS NULL "
                 + "   OR e.createdBy.id = :userId "
                 + "   OR ue.sharedWith.id = :userId "
                 + "GROUP BY e.id ")
    Page<Object[]> findAllExerciseSummariesAvailableForUser(@Param("userId") String userId, Pageable pageable);

    @Query(value = "SELECT e "
                 + "FROM ExercisesEntity e "
                 + "LEFT JOIN UserExercisesEntity ue ON e.id = ue.exercise.id "
                 + "WHERE e.id = :exerciseId "
                 + "  AND (e.createdBy.id IS NULL "
                 + "   OR e.createdBy.id = :userId "
                 + "   OR ue.sharedWith.id = :userId) "
                 + "GROUP BY e.id ")
    Optional<ExercisesEntity> findExerciseByUserAndId(@Param("userId") String userId, @Param("exerciseId") Long exerciseId);

    @Modifying
    @Transactional
    void deleteByIdAndCreatedBy_Id(Long id, String createdBy);

}
