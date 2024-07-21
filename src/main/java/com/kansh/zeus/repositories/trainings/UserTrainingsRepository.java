package com.kansh.zeus.repositories.trainings;

import com.kansh.zeus.domain.entities.trainings.UserTrainingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTrainingsRepository extends JpaRepository<UserTrainingsEntity, Long> {
}
