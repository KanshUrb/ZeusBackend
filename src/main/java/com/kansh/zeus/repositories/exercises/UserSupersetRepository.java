package com.kansh.zeus.repositories.exercises;

import com.kansh.zeus.domain.entities.exercises.UserSupersetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserSupersetRepository extends JpaRepository<UserSupersetsEntity, Long> {
    List<UserSupersetsEntity> findAllBySuperset_Id(Long supersetId);

    void deleteBySuperset_IdAndSharedWith_Hash(Long id, String hash);

    @Modifying
    @Transactional
    void deleteBySuperset_Id(Long supersetId);
}
