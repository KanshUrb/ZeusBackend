package com.kansh.zeus.service;

import com.kansh.zeus.domain.entities.body_params.BodyParamsEntity;

import java.util.List;
import java.util.Optional;

public interface BodyParamService {

    boolean isExists(Long id);

    Optional<BodyParamsEntity> findBodyParams(String userId, Long id);

    BodyParamsEntity saveBodyParams(BodyParamsEntity bodyParamsEntity);

    void deleteBodyParams(Long id);

    List<BodyParamsEntity> findAllBodyParamsForUser(String userId);

    Float calculateBMI(Integer height, Float weight);

    Float calculateLBM(Float weight, Float bodyFat);

    Float calculateBF(Integer gender, Integer height, Float waist, Float neck, Float hip);

}
