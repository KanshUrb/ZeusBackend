package com.kansh.zeus.repositories.trainings;

import com.kansh.zeus.domain.entities.trainings.TrainingsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingHistoryRepository extends JpaRepository<TrainingsHistoryEntity, Long> {
}

