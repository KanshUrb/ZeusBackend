package com.kansh.zeus.service;

import com.kansh.zeus.domain.dto.trainings.TrainingItemDto;
import com.kansh.zeus.domain.dto.trainings.TrainingSummaryDto;
import com.kansh.zeus.domain.dto.trainings.TrainingsDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;

import java.util.List;
import java.util.Optional;

public interface TrainingService {

    List<TrainingSummaryDto> getTrainingsSummariesAvailableForUser(String userId);

    Optional<TrainingsDto> getTrainingById(String userId, Long id);

    TrainingsDto createTraining(UsersEntity user, String name, String note, List<TrainingItemDto> trainingItems, List<String> sharedWith);

    void deleteTraining(Long trainingId, String userId);

}
