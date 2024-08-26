package com.kansh.zeus.repository.trainings;

import com.kansh.zeus.domain.entities.trainings.TrainingsHistoryItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingHistoryItemRepository extends JpaRepository<TrainingsHistoryItemsEntity, Long> {
}

