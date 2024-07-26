package com.kansh.zeus.services.impl;

import com.kansh.zeus.domain.dto.exercises.ExercisesDto;
import com.kansh.zeus.domain.dto.exercises.SupersetOutputDto;
import com.kansh.zeus.domain.dto.exercises.SupersetsDto;
import com.kansh.zeus.domain.dto.trainings.*;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsSeriesEntity;
import com.kansh.zeus.domain.entities.trainings.UserTrainingsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.repositories.exercises.ExerciseRepository;
import com.kansh.zeus.repositories.exercises.SupersetRepository;
import com.kansh.zeus.repositories.trainings.TrainingItemRepository;
import com.kansh.zeus.repositories.trainings.TrainingItemSeriesRepository;
import com.kansh.zeus.repositories.trainings.TrainingRepository;
import com.kansh.zeus.repositories.trainings.UserTrainingsRepository;
import com.kansh.zeus.services.TrainingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingItemRepository trainingItemRepository;
    private final UserTrainingsRepository userTrainingsRepository;
    private final ExerciseRepository exerciseRepository;
    private final SupersetRepository supersetRepository;
    private final TrainingItemSeriesRepository trainingItemSeriesRepository;
    private final Mapper<ExercisesEntity, ExercisesDto> exerciseMapper;

    public TrainingServiceImpl(TrainingRepository trainingRepository, UserTrainingsRepository userTrainingsRepository, ExerciseRepository exerciseRepository, SupersetRepository supersetRepository, TrainingItemRepository trainingItemRepository, TrainingItemSeriesRepository trainingItemSeriesRepository, Mapper<ExercisesEntity, ExercisesDto> exerciseMapper) {
        this.trainingRepository = trainingRepository;
        this.trainingItemRepository = trainingItemRepository;
        this.userTrainingsRepository = userTrainingsRepository;
        this.exerciseRepository = exerciseRepository;
        this.supersetRepository = supersetRepository;
        this.trainingItemSeriesRepository = trainingItemSeriesRepository;
        this.exerciseMapper = exerciseMapper;
    }

    @Override
    public List<TrainingSummaryDto> getTrainingsSummariesAvailableForUser(String userId) {
        List<TrainingsEntity> trainingEntities = trainingRepository.findAllTrainingSummariesAvailableForUser(userId);
        List<TrainingSummaryDto> trainingSummaries = new ArrayList<>();
        for(TrainingsEntity trainingEntity : trainingEntities) {
            trainingSummaries.add(TrainingSummaryDto.builder()
                    .id(trainingEntity.getId())
                    .name(trainingEntity.getName())
                    .note(trainingEntity.getNote())
                    .build());
        }
        return trainingSummaries;
    }

    @Override
    public Optional<TrainingsDto> getTrainingById(String userId, Long id) {
        log.info("TrainingService::getTrainingById START userId = {}, id = {}", userId, id);
        Optional<TrainingsEntity> training = trainingRepository.findTrainingByUserAndId(userId, id);
        if (training.isEmpty()) {
            return Optional.empty();
        }
        TrainingsDto trainingDto = new TrainingsDto();
        trainingDto.setId(training.get().getId());
        trainingDto.setName(training.get().getName());
        trainingDto.setNote(training.get().getNote());

        List<TrainingItemDto> trainingItems = new ArrayList<>();
        List<TrainingsItemsEntity> trainingItemsEntity = trainingRepository.findTrainingItemsByTrainingId(id);
        log.info(trainingItemsEntity.toString());
        for(TrainingsItemsEntity trainingItemEntity : trainingItemsEntity) {
            List<TrainingsItemsSeriesEntity> trainingItemsSeriesEntity = trainingRepository.findTrainingItemsSeriesByTrainingItemsId(trainingItemEntity.getId());
            List<SeriesDto> series = new ArrayList<>();
            for(TrainingsItemsSeriesEntity trainingItemsSeries : trainingItemsSeriesEntity) {
                series.add(SeriesDto.builder()
                        .id(trainingItemsSeries.getId())
                        .repetitions(trainingItemsSeries.getRepetitions())
                        .weight1(trainingItemsSeries.getWeight1())
                        .weight2(trainingItemsSeries.getWeight2())
                        .build());
            }
            if(!isNull(trainingItemEntity.getExercise())) {
                log.info(trainingItemEntity.getExercise().toString());
            }
            log.info(trainingItemEntity.toString());
            log.info(trainingItemEntity.toString());
            TrainingItemDto trainingItem = TrainingItemDto.builder()
                    .id(trainingItemEntity.getId())
                    .itemType(trainingItemEntity.getItemType())
                    .exercise(!isNull(trainingItemEntity.getExercise()) ? ExercisesDto.builder()
                            .id(trainingItemEntity.getExercise().getId())
                            .name(trainingItemEntity.getExercise().getName())
                            .description(trainingItemEntity.getExercise().getDescription())
                            .muscleGroup(trainingItemEntity.getExercise().getMuscleGroup())
                            .difficultyLevel(trainingItemEntity.getExercise().getDifficultyLevel())
                            .videoUrl(trainingItemEntity.getExercise().getVideoUrl())
                            .rate(trainingItemEntity.getExercise().getRate())
                            .build() : null)
                    .superset(!isNull(trainingItemEntity.getSuperset()) ? SupersetOutputDto.builder()
                            .id(trainingItemEntity.getSuperset().getId())
                            .name(trainingItemEntity.getSuperset().getName())
                            .exercise1(exerciseMapper.mapTo(trainingItemEntity.getSuperset().getExercise1()))
                            .exercise2(exerciseMapper.mapTo(trainingItemEntity.getSuperset().getExercise2()))
                            .rate(trainingItemEntity.getSuperset().getRate())
                            .build() : null)
                    .series(series)
                    .build();
            trainingItems.add(trainingItem);
        }
        trainingDto.setTrainingItems(trainingItems);
        log.info("TrainingService::getTrainingById STOP, trainingDto = {}", trainingDto);
        return Optional.of(trainingDto);
    }

    @Override
    @Transactional
    public TrainingsDto createTraining(UsersEntity user, String name, String note, List<TrainingItemDto> trainingItems, List<String> sharedWith) {
        log.info("TrainingService::createTraining START");
        TrainingsDto trainingDto = new TrainingsDto();

        TrainingsEntity training = TrainingsEntity.builder()
                .name(name)
                .note(note)
                .createdBy(user)
                .build();
        TrainingsEntity savedTraining = trainingRepository.save(training);
        trainingDto.setId(savedTraining.getId());
        trainingDto.setName(savedTraining.getName());
        trainingDto.setNote(savedTraining.getNote());

        List<UserTrainingsEntity> userTrainingsEntities = new ArrayList<>();
        for (String sharedWithId : sharedWith) {
            userTrainingsEntities.add(UserTrainingsEntity.builder()
                    .user(user)
                    .training(savedTraining)
                    .sharedWith(UsersEntity.builder().id(sharedWithId).build())
                    .build());
        }
        log.info("userTrainingsEntities: {}", userTrainingsEntities);

        if (!userTrainingsEntities.isEmpty()) {
            userTrainingsRepository.saveAll(userTrainingsEntities);
        }
        List<TrainingItemDto> trainingItemsOutput = new ArrayList<>();
        for (TrainingItemDto trainingItem : trainingItems) {
            TrainingsItemsEntity trainingItemsEntity = new TrainingsItemsEntity();
            Optional<ExercisesEntity> exercise;
            Optional<SupersetsEntity> superset;

            if (trainingItem.getItemType() == 1) {
                exercise = exerciseRepository.findExerciseByUserAndId(user.getId(), trainingItem.getExercise().getId());
                if (exercise.isPresent()) {
                    trainingItemsEntity.setTraining(savedTraining);
                    trainingItemsEntity.setExercise(exercise.get());
                    trainingItemsEntity.setSuperset(null);
                    trainingItemsEntity.setItemType(1);
                    trainingItemsEntity = trainingItemRepository.save(trainingItemsEntity);

                    int i = 0;
                    List<SeriesDto> seriesDto = new ArrayList<>();
                    for (SeriesDto series : trainingItem.getSeries()) {
                        TrainingsItemsSeriesEntity trainingItemsSeriesEntity = TrainingsItemsSeriesEntity.builder()
                                .trainingItem(trainingItemsEntity)
                                .seriesNumber(i)
                                .repetitions(series.getRepetitions())
                                .weight1(series.getWeight1())
                                .weight2(series.getWeight2())
                                .build();
                        trainingItemsSeriesEntity = trainingItemSeriesRepository.save(trainingItemsSeriesEntity);
                        seriesDto.add(SeriesDto.builder()
                                .id(trainingItemsSeriesEntity.getId())
                                .repetitions(trainingItemsSeriesEntity.getRepetitions())
                                .weight1(trainingItemsSeriesEntity.getWeight1())
                                .weight2(trainingItemsSeriesEntity.getWeight2())
                                .build());
                        i++;
                    }

                    trainingItemsOutput.add(TrainingItemDto.builder()
                            .id(trainingItemsEntity.getId())
                            .itemType(1)
                            .exercise(trainingItem.getExercise())
                            .series(seriesDto)
                            .build());
                }
            }
            if (trainingItem.getItemType() == 2) {
                superset = supersetRepository.findSupersetByUserAndId(user.getId(), trainingItem.getSuperset().getId());
                if (superset.isPresent()) {
                    trainingItemsEntity.setTraining(savedTraining);
                    trainingItemsEntity.setExercise(null);
                    trainingItemsEntity.setSuperset(superset.get());
                    trainingItemsEntity.setItemType(2);
                    trainingItemsEntity = trainingItemRepository.save(trainingItemsEntity);

                    int i = 0;
                    List<SeriesDto> seriesDto = new ArrayList<>();
                    for (SeriesDto series : trainingItem.getSeries()) {
                        TrainingsItemsSeriesEntity trainingItemsSeriesEntity = TrainingsItemsSeriesEntity.builder()
                                .trainingItem(trainingItemsEntity)
                                .seriesNumber(i)
                                .repetitions(series.getRepetitions())
                                .weight1(series.getWeight1())
                                .weight2(series.getWeight2())
                                .build();
                        trainingItemsSeriesEntity = trainingItemSeriesRepository.save(trainingItemsSeriesEntity);
                        seriesDto.add(SeriesDto.builder()
                                .id(trainingItemsSeriesEntity.getId())
                                .repetitions(trainingItemsSeriesEntity.getRepetitions())
                                .weight1(trainingItemsSeriesEntity.getWeight1())
                                .weight2(trainingItemsSeriesEntity.getWeight2())
                                .build());
                        i++;
                    }


                    trainingItemsOutput.add(TrainingItemDto.builder()
                            .id(trainingItemsEntity.getId())
                            .itemType(2)
                            .superset(trainingItem.getSuperset())
                            .series(seriesDto)
                            .build());
                }
            }

        }
        trainingDto.setTrainingItems(trainingItemsOutput);

        log.info("trainingDto: {}", trainingDto);
        log.info("TrainingService::createTraining STOP");
        return trainingDto;
    }

    @Override
    @Transactional
    public void deleteTraining(Long trainingId, String userId) {
        log.info("TrainingService::deleteTraining START trainingId = {}, userId = {}", trainingId, userId);
        trainingRepository.deleteByIdAndCreatedBy_Id(trainingId, userId);
        log.info("TrainingService::deleteTraining STOP");
    }

}
