package com.kansh.zeus.repository.trainings;

import com.kansh.zeus.domain.entities.trainings.TrainingsItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingItemRepository extends JpaRepository<TrainingsItemsEntity, Long> {

    List<TrainingsItemsEntity> findByTrainingId(Long trainingId);
}

