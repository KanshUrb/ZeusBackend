package com.kansh.zeus.services;

import com.kansh.zeus.domain.entities.trainings.TrainingsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;

public interface TrainingService {

    Page<Object[]> getTrainingsSummariesAvailableForUser(String userId, Pageable pageable);

    Optional<TrainingsEntity> getTrainingByUserAndId(String userId, Long id);

    TrainingsEntity createTraining(TrainingsEntity training, UsersEntity user, List<Pair<Long, Integer>> exercises, List<String> sharedWith);

    List<TrainingsItemsEntity> getTrainingItemsByTrainingId(Long trainingId);

}
