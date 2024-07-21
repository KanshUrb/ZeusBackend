package com.kansh.zeus.services.impl;

import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.exercises.UserExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.UserSupersetsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repositories.exercises.ExerciseRepository;
import com.kansh.zeus.repositories.exercises.SupersetRepository;
import com.kansh.zeus.repositories.exercises.UserExerciseRepository;
import com.kansh.zeus.repositories.exercises.UserSupersetRepository;
import com.kansh.zeus.services.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserExerciseRepository userExerciseRepository;
    private final SupersetRepository supersetRepository;
    private final UserSupersetRepository userSupersetRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, UserExerciseRepository userExerciseRepository,
                               SupersetRepository supersetRepository, UserSupersetRepository userSupersetRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userExerciseRepository = userExerciseRepository;
        this.supersetRepository = supersetRepository;
        this.userSupersetRepository = userSupersetRepository;
    }

    @Override
    public Page<Object[]> getExercisesSummariesAvailableForUser(String userId, Pageable pageable) {
        return exerciseRepository.findAllExerciseSummariesAvailableForUser(userId, pageable);
    }

    @Override
    public Optional<ExercisesEntity> getExerciseByUserAndId(String userId, Long id) {
        return exerciseRepository.findExerciseByUserAndId(userId, id);
    }

    @Override
    public ExercisesEntity createExercise(ExercisesEntity exercise, UsersEntity user, List<String> sharedWith) {
        log.info("ExerciseService::createExercise START");

        exercise.setCreatedBy(user);
        ExercisesEntity savedExercise = exerciseRepository.save(exercise);

        List<UserExercisesEntity> userExercisesEntities = new ArrayList<>();
        for (String sharedWithId : sharedWith) {
            userExercisesEntities.add(UserExercisesEntity.builder()
                    .exercise(savedExercise)
                    .user(user)
                    .sharedWith(UsersEntity.builder().id(sharedWithId).build())
                    .build());
        }
        log.info("userExercisesEntities: {}", userExercisesEntities);

        if (!userExercisesEntities.isEmpty()) {
            userExerciseRepository.saveAll(userExercisesEntities);
        }

        log.info("ExerciseService::createExercise STOP");
        return savedExercise;
    }

    @Override
    public void deleteExercise(Long exerciseId, String userId) {
        log.info("ExerciseService::deleteExercise START exerciseId = {}, userId = {}", exerciseId, userId);
        exerciseRepository.deleteByIdAndCreatedBy_Id(exerciseId, userId);
        log.info("ExerciseService::deleteExercise STOP");
    }

    @Override
    public ExercisesEntity updateExercise(ExercisesEntity exercisesEntity) {
        log.info("ExerciseService::updateExercise START");
        ExercisesEntity updatedExercise = exerciseRepository.save(exercisesEntity);
        log.info("ExerciseService::updateExercise STOP");
        return updatedExercise;
    }

    @Override
    public void rateExercise(Long exerciseId, Integer rate) {
        log.info("ExerciseService::rateExercise START exerciseId = {}, rate = {}", exerciseId, rate);

        Optional<ExercisesEntity> exercise = exerciseRepository.findById(exerciseId);
        exercise.ifPresent(exerciseEntity -> {
            log.info("Exercise = {}", exerciseEntity);
            Float newRate = calcRate(exerciseEntity.getRate(), exerciseEntity.getUserCounter(), rate);
            exerciseEntity.setRate(newRate);
            exerciseEntity.setUserCounter(exerciseEntity.getUserCounter() + 1);
            exerciseRepository.save(exerciseEntity);
        });

        log.info("ExerciseService::rateExercise STOP");
    }

    @Override
    public Page<Object[]> getSupersetsSummariesAvailableForUser(String userId, Pageable pageable) {
        return supersetRepository.findAllSupersetsSummariesAvailableForUser(userId, pageable);
    }

    @Override
    public Optional<SupersetsEntity> getSupersetByUserAndId(String userId, Long id) {
        return supersetRepository.findSupersetByUserAndId(userId, id);
    }

    @Override
    public SupersetsEntity createSuperset(SupersetsEntity superset, UsersEntity user, List<String> sharedWith) {
        log.info("ExerciseService::createSuperset START");

        superset.setCreatedBy(user);
        SupersetsEntity savedSuperset = supersetRepository.save(superset);

        List<UserSupersetsEntity> userSupersetsEntities = new ArrayList<>();
        for (String sharedWithId : sharedWith) {
            userSupersetsEntities.add(UserSupersetsEntity.builder()
                    .superset(savedSuperset)
                    .user(user)
                    .sharedWith(UsersEntity.builder().id(sharedWithId).build())
                    .build());
        }
        log.info("userSupersetsEntities: {}", userSupersetsEntities);

        if (!userSupersetsEntities.isEmpty()) {
            userSupersetRepository.saveAll(userSupersetsEntities);
        }

        log.info("ExerciseService::createSuperset STOP");
        return savedSuperset;
    }

    @Override
    public void deleteSuperset(Long supersetId, String userId) {
        log.info("ExerciseService::deleteSuperset START supersetId = {}, userId = {}", supersetId, userId);
        supersetRepository.deleteByIdAndCreatedBy_Id(supersetId, userId);
        log.info("ExerciseService::deleteSuperset STOP");
    }

    @Override
    public SupersetsEntity updateSuperset(SupersetsEntity supersetsEntity) {
        log.info("ExerciseService::updateSuperset START");
        SupersetsEntity updatedSuperset = supersetRepository.save(supersetsEntity);
        log.info("ExerciseService::updateSuperset STOP");
        return updatedSuperset;
    }

    @Override
    public void rateSuperset(Long supersetId, Integer rate) {
        log.info("ExerciseService::rateSuperset START supersetId = {}, rate = {}", supersetId, rate);

        Optional<SupersetsEntity> superset = supersetRepository.findById(supersetId);
        superset.ifPresent(supersetEntity -> {
            log.info("Superset = {}", supersetEntity);
            Float newRate = calcRate(supersetEntity.getRate(), supersetEntity.getUserCounter(), rate);
            supersetEntity.setRate(newRate);
            supersetEntity.setUserCounter(supersetEntity.getUserCounter() + 1);
            supersetRepository.save(supersetEntity);
        });

        log.info("ExerciseService::rateSuperset STOP");
    }

    private Float calcRate(Float currentRate, Integer userCounter, Integer rate) {
        return ((currentRate * userCounter) + rate) / (userCounter + 1);
    }

    @Override
    public ExercisesEntity addExerciseTechnical(ExercisesEntity exercisesEntity) {
        return exerciseRepository.save(exercisesEntity);
    }

    @Override
    public SupersetsEntity addSupersetTechnical(SupersetsEntity supersetsEntity) {
        return supersetRepository.save(supersetsEntity);
    }
}
