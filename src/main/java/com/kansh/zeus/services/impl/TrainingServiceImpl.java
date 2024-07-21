package com.kansh.zeus.services.impl;

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
import com.kansh.zeus.services.TrainingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingItemRepository trainingItemRepository;
    private final UserTrainingsRepository userTrainingsRepository;
    private final ExerciseRepository exerciseRepository;
    private final SupersetRepository supersetRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository, UserTrainingsRepository userTrainingsRepository, ExerciseRepository exerciseRepository, SupersetRepository supersetRepository, TrainingItemRepository trainingItemRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingItemRepository = trainingItemRepository;
        this.userTrainingsRepository = userTrainingsRepository;
        this.exerciseRepository = exerciseRepository;
        this.supersetRepository = supersetRepository;
    }

    @Override
    public Page<Object[]> getTrainingsSummariesAvailableForUser(String userId, Pageable pageable) {
        return trainingRepository.findAllTrainingSummariesAvailableForUser(userId, pageable);
    }

    @Override
    public Optional<TrainingsEntity> getTrainingByUserAndId(String userId, Long id) {
        return trainingRepository.findTrainingByUserAndId(userId, id);
    }

    @Override
    public List<TrainingsItemsEntity> getTrainingItemsByTrainingId(Long trainingId) {
        return trainingItemRepository.findByTrainingId(trainingId);
    }

    //Uzytkownik na kliencie dodaje rzeczy do treningu itp, do serwera wysle
    // zakonczony trening w formie jakiegos jsona czy cos

    @Override
    @Transactional
    public TrainingsEntity createTraining(TrainingsEntity training, UsersEntity user, List<Pair<Long, Integer>> exercises, List<String> sharedWith) {
        log.info("TrainingService::createTraining START");
        training.setCreatedBy(user);
        TrainingsEntity savedTraining = trainingRepository.save(training);
        log.debug("KURWA: {}, {}, {}, {}", training, user, exercises, sharedWith);
        for (Pair<Long, Integer> item : exercises) {
            List<TrainingsItemsEntity> trainingItems = new ArrayList<>();
            TrainingsItemsEntity trainingItemsEntity = new TrainingsItemsEntity();
            Optional<ExercisesEntity> exercise;
            Optional<SupersetsEntity> superset;

            if (item.getSecond() == 1) { //exercise
                log.debug("POJEBIE MNIE");
                exercise = exerciseRepository.findExerciseByUserAndId(user.getId(), item.getFirst());
                log.debug(exercise.toString());
                if (exercise.isPresent()) {
                    trainingItemsEntity.setTraining(savedTraining);
                    trainingItemsEntity.setExercise(exercise.get());
                    trainingItemsEntity.setSuperset(null);
                    trainingItemsEntity.setItemType(1);
                    trainingItems.add(trainingItemsEntity);
                }
            }
            if (item.getSecond() == 2) {
                superset = supersetRepository.findSupersetByUserAndId(user.getId(), item.getFirst());
                if (superset.isPresent()) {
                    trainingItemsEntity.setTraining(savedTraining);
                    trainingItemsEntity.setExercise(null);
                    trainingItemsEntity.setSuperset(superset.get());
                    trainingItemsEntity.setItemType(2);
                    trainingItems.add(trainingItemsEntity);
                }
            }
            log.info("JAPIERDOLE: {}", trainingItemsEntity);
            trainingItemRepository.save(trainingItemsEntity);
            log.info("trainingItemRepository: {}", trainingItemsEntity);
        }

        List<UserTrainingsEntity> userTrainingsEntities = new ArrayList<>();
        for (String sharedWithId : sharedWith) {
            UserTrainingsEntity userTrainingsEntity = UserTrainingsEntity.builder()
                    .user(user)
                    .training(savedTraining)
                    .sharedWith(UsersEntity.builder().id(sharedWithId).build())
                    .build();
            userTrainingsEntities.add(userTrainingsEntity);
        }
        log.info("userTrainingsEntities: {}", userTrainingsEntities);

        if (!userTrainingsEntities.isEmpty()) {
           userTrainingsRepository.saveAll(userTrainingsEntities);
        }
        log.info("TrainingService::createTraining STOP");
        return savedTraining;
    }

 /*   Training:
        - id
        - name,
        - note,
        - created_by

    Trainings_items:
        - id (unique_id)
        - training_id (FK)
        - item-type (exercise/superset)
        - exercise_id
        - superset_id

    Training-items-series
        - unique id
        - id do training_items

    Create Training ->
    Insert Training, List Training_items, List to List training item_serues
*/
}
