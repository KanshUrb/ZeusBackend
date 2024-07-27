package com.kansh.zeus.repositories.exercises;

import com.kansh.zeus.domain.entities.exercises.UserSupersetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSupersetRepository extends JpaRepository<UserSupersetsEntity, Long> {
    List<UserSupersetsEntity> findAllBySuperset_Id(Long supersetId);
}
