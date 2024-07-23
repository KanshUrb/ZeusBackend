package com.kansh.zeus.services;

import com.kansh.zeus.domain.dto.trainings.TrainingItemDto;
import com.kansh.zeus.domain.dto.trainings.TrainingsDto;
import com.kansh.zeus.domain.entities.trainings.TrainingsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TrainingService {

    Page<Object[]> getTrainingsSummariesAvailableForUser(String userId, Pageable pageable);

    Optional<TrainingsEntity> getTrainingByUserAndId(String userId, Long id);

    TrainingsDto createTraining(UsersEntity user, String name, String note, List<TrainingItemDto> trainingItems, List<String> sharedWith);

    List<TrainingsItemsEntity> getTrainingItemsByTrainingId(Long trainingId);

}
