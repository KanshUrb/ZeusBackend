package com.kansh.zeus.repositories.body_params;

import com.kansh.zeus.domain.entities.body_params.BodyParamsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BodyParamRepository extends JpaRepository<BodyParamsEntity, Long> {

    Optional<BodyParamsEntity> findByIdAndUserId(Long id, String userId);

    List<BodyParamsEntity> findAllByUserId(String userId);
}
