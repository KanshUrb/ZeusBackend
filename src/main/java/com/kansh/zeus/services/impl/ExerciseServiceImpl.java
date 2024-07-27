package com.kansh.zeus.services.impl;

import com.kansh.zeus.domain.dto.exercises.ExerciseWrapperDto;
import com.kansh.zeus.domain.dto.exercises.ExercisesDto;
import com.kansh.zeus.domain.dto.friends.FriendDto;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.exercises.UserExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.UserSupersetsEntity;
import com.kansh.zeus.domain.entities.friends.FriendEntity;
import com.kansh.zeus.domain.entities.trainings.UserTrainingsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repositories.exercises.ExerciseRepository;
import com.kansh.zeus.repositories.exercises.SupersetRepository;
import com.kansh.zeus.repositories.exercises.UserExerciseRepository;
import com.kansh.zeus.repositories.exercises.UserSupersetRepository;
import com.kansh.zeus.repositories.trainings.UserTrainingsRepository;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserExerciseRepository userExerciseRepository;
    private final SupersetRepository supersetRepository;
    private final UserSupersetRepository userSupersetRepository;
    private final UserTrainingsRepository userTrainingsRepository;
    private final UserRepository userRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, UserExerciseRepository userExerciseRepository,
                               SupersetRepository supersetRepository, UserSupersetRepository userSupersetRepository, UserTrainingsRepository userTrainingsRepository, UserRepository userRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userExerciseRepository = userExerciseRepository;
        this.supersetRepository = supersetRepository;
        this.userSupersetRepository = userSupersetRepository;
        this.userTrainingsRepository = userTrainingsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<UsersEntity> getSharedWith(int itemType, Long itemId) {
        log.info("ExerciseService::getSharedWith START itemType = {}, itemId = {}", itemType == 1 ? "Exercise" : itemType == 2 ? "Superset" : "Training", itemId);

        return switch (itemType) {
            case 1 -> userExerciseRepository.findAllByExercise_Id(itemId).stream().map(UserExercisesEntity::getSharedWith).toList();
            case 2 -> userSupersetRepository.findAllBySuperset_Id(itemId).stream().map(UserSupersetsEntity::getSharedWith).toList();
            case 3 -> userTrainingsRepository.findAllByTraining_Id(itemId).stream().map(UserTrainingsEntity::getSharedWith).toList();
            default -> {
                log.info("ExercisesService::getSharedWith ERROR Unexpected itemType: {}", itemType);
                throw new IllegalStateException("Unexpected itemType: " + itemType);
            }
        };
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
    public Optional<ExercisesEntity> getExerciseById(Long id) {
        return exerciseRepository.findById(id);
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
                    .sharedWith(userRepository.findByHash(sharedWithId).orElseThrow())
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
    @Transactional
    public ExerciseWrapperDto updateExercise(String userId, ExercisesEntity exercise,  ExerciseWrapperDto exerciseWrapperDto) {
        log.info("ExerciseService::updateExercise START");

        exercise.setName(exerciseWrapperDto.getExercise().getName());
        exercise.setDescription(exerciseWrapperDto.getExercise().getDescription());
        exercise.setMuscleGroup(exerciseWrapperDto.getExercise().getMuscleGroup());
        exercise.setDifficultyLevel(exerciseWrapperDto.getExercise().getDifficultyLevel());
        exercise.setVideoUrl(exerciseWrapperDto.getExercise().getVideoUrl());
        ExercisesEntity savedExercise = exerciseRepository.save(exercise);

        List<UsersEntity> userExercisesEntities = getSharedWith(1, savedExercise.getId());
        Set<String> currentUserHashes = userExercisesEntities.stream().map(UsersEntity::getHash).collect(Collectors.toSet());
        Set<String> newUserHashes = exerciseWrapperDto.getSharedWith().stream().map(FriendDto::getHash).collect(Collectors.toSet());

        currentUserHashes.stream()
                .filter(hash -> !newUserHashes.contains(hash))
                .forEach(hash -> userExerciseRepository.deleteByExercise_IdAndSharedWith_Hash(savedExercise.getId(), hash));

        newUserHashes.stream()
                .filter(hash -> !currentUserHashes.contains(hash))
                .forEach(hash -> userExerciseRepository.save(UserExercisesEntity.builder()
                        .exercise(savedExercise)
                        .sharedWith(userRepository.findByHash(hash).orElseThrow())
                        .build()));

        ExerciseWrapperDto output = ExerciseWrapperDto.builder()
                .exercise(ExercisesDto.builder()
                        .id(savedExercise.getId())
                        .name(savedExercise.getName())
                        .description(savedExercise.getDescription())
                        .muscleGroup(savedExercise.getMuscleGroup())
                        .difficultyLevel(savedExercise.getDifficultyLevel())
                        .videoUrl(savedExercise.getVideoUrl())
                        .rate(savedExercise.getRate())
                        .build())
                .sharedWith(exerciseWrapperDto.getSharedWith())
                .build();


        log.info("ExerciseService::updateExercise STOP, exerciseWrapperDto = {}", output);
        return output;
    }

    @Override
    public Float rateExercise(Long exerciseId, Integer rate) {
        log.info("ExerciseService::rateExercise START exerciseId = {}, rate = {}", exerciseId, rate);

        Float newRate;
        Optional<ExercisesEntity> exercise = exerciseRepository.findById(exerciseId);
        if (exercise.isPresent()) {
            log.info("Exercise = {}", exercise.get());
            newRate = calcRate(exercise.get().getRate(), exercise.get().getUserCounter(), rate);
            exercise.get().setRate(newRate);
            exercise.get().setUserCounter(exercise.get().getUserCounter() + 1);
            exerciseRepository.save(exercise.get());
        } else {
            throw new RuntimeException("Exercise not found");
        }

        log.info("ExerciseService::rateExercise STOP, new rate = {}", newRate);
        return newRate;
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
    public Optional<SupersetsEntity> getSupersetById(Long id) {
        return supersetRepository.findById(id);
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
    public Float rateSuperset(Long supersetId, Integer rate) {
        log.info("ExerciseService::rateSuperset START supersetId = {}, rate = {}", supersetId, rate);

        Float newRate;
        Optional<SupersetsEntity> superset = supersetRepository.findById(supersetId);
        if (superset.isPresent()) {
            log.info("Superset = {}", superset.get());
            newRate = calcRate(superset.get().getRate(), superset.get().getUserCounter(), rate);
            superset.get().setRate(newRate);
            superset.get().setUserCounter(superset.get().getUserCounter() + 1);
            supersetRepository.save(superset.get());
        } else {
            throw new RuntimeException("Superset not found");
        }

        log.info("ExerciseService::rateSuperset STOP, new rate = {}", newRate);
        return newRate;
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
