package com.kansh.zeus.services;

import com.kansh.zeus.domain.dto.exercises.ExerciseWrapperDto;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {

    List<UsersEntity> getSharedWith(int itemType, Long itemId);

    Page<Object[]> getExercisesSummariesAvailableForUser(String userId, Pageable pageable);

    Optional<ExercisesEntity> getExerciseByUserAndId(String userId, Long id);

    ExercisesEntity createExercise(ExercisesEntity exercise, UsersEntity user, List<String> sharedWith);

    void deleteExercise(Long exerciseId, String userId);

    ExerciseWrapperDto updateExercise(String userId, ExercisesEntity exercise, ExerciseWrapperDto exerciseWrapperDto);

    Page<Object[]> getSupersetsSummariesAvailableForUser(String userId, Pageable pageable);

    Optional<SupersetsEntity> getSupersetByUserAndId(String userId, Long id);

    Optional<ExercisesEntity> getExerciseById(Long id);

    Optional<SupersetsEntity> getSupersetById(Long id);

    SupersetsEntity createSuperset(SupersetsEntity superset, UsersEntity user, List<String> sharedWith);

    void deleteSuperset(Long supersetId, String userId);

    SupersetsEntity updateSuperset(SupersetsEntity supersetsEntity);

    Float rateExercise(Long exerciseId, Integer rate);

    Float rateSuperset(Long supersetId, Integer rate);

    ExercisesEntity addExerciseTechnical(ExercisesEntity exercisesEntity);

    SupersetsEntity addSupersetTechnical(SupersetsEntity supersetsEntity);
}
