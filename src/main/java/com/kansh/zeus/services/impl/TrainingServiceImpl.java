package com.kansh.zeus.services.impl;

import com.kansh.zeus.domain.dto.trainings.SeriesDto;
import com.kansh.zeus.domain.dto.trainings.TrainingItemDto;
import com.kansh.zeus.domain.dto.trainings.TrainingsDto;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsSeriesEntity;
import com.kansh.zeus.domain.entities.trainings.UserTrainingsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
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
    private final TrainingItemSeriesRepository trainingItemSeriesRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository, UserTrainingsRepository userTrainingsRepository, ExerciseRepository exerciseRepository, SupersetRepository supersetRepository, TrainingItemRepository trainingItemRepository, TrainingItemSeriesRepository trainingItemSeriesRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingItemRepository = trainingItemRepository;
        this.userTrainingsRepository = userTrainingsRepository;
        this.exerciseRepository = exerciseRepository;
        this.supersetRepository = supersetRepository;
        this.trainingItemSeriesRepository = trainingItemSeriesRepository;
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
    public TrainingsDto createTraining(UsersEntity user, String name, String note, List<TrainingItemDto> trainingItems, List<String> sharedWith) {
        log.info("TrainingService::createTraining START");
        log.info("KURWA {}", trainingItems);
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

}
