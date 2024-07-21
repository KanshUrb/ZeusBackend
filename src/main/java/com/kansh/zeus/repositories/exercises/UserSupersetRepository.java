package com.kansh.zeus.repositories.exercises;

import com.kansh.zeus.domain.entities.exercises.UserSupersetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSupersetRepository extends JpaRepository<UserSupersetsEntity, Long> {
}
