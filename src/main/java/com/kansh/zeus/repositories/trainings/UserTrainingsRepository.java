package com.kansh.zeus.repositories.trainings;

import com.kansh.zeus.domain.entities.trainings.UserTrainingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTrainingsRepository extends JpaRepository<UserTrainingsEntity, Long> {
    List<UserTrainingsEntity> findAllByTraining_Id(Long trainingId);
}
