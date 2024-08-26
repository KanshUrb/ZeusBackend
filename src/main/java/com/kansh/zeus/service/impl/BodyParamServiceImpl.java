package com.kansh.zeus.service.impl;

import com.kansh.zeus.domain.entities.body_params.BodyParamsEntity;
import com.kansh.zeus.repository.body_params.BodyParamRepository;
import com.kansh.zeus.service.BodyParamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BodyParamServiceImpl implements BodyParamService {

    private final BodyParamRepository bodyParamRepository;

    @Override
    public boolean isExists(Long id) {
        return bodyParamRepository.existsById(id);
    }

    @Override
    public Optional<BodyParamsEntity> findBodyParams(String userId, Long id) {
        return bodyParamRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public BodyParamsEntity saveBodyParams(BodyParamsEntity bodyParamsEntity) {
        bodyParamsEntity.setDate(bodyParamsEntity.getDate() == null ? LocalDate.now() : bodyParamsEntity.getDate());
        bodyParamsEntity.setBf(calculateBF(bodyParamsEntity.getUser().getGender(), bodyParamsEntity.getHeight(), bodyParamsEntity.getWaist(), bodyParamsEntity.getNeck(), bodyParamsEntity.getHip()));
        bodyParamsEntity.setLbm(calculateLBM(bodyParamsEntity.getWeight(), bodyParamsEntity.getBf()));
        bodyParamsEntity.setBmi(calculateBMI(bodyParamsEntity.getHeight(), bodyParamsEntity.getWeight()));
        return bodyParamRepository.save(bodyParamsEntity);
    }

    @Override
    public void deleteBodyParams(Long bodyParamId) {
        bodyParamRepository.deleteById(bodyParamId);
    }

    @Override
    public List<BodyParamsEntity> findAllBodyParamsForUser(String userId) {
        return bodyParamRepository.findAllByUserIdOrderByIdDesc(userId);
    }

    @Override
    public Float calculateBMI(Integer height, Float weight) {
        float bmi = weight / ((height / 100.0f) * (height / 100.0f));
        return bmi > 100 || bmi < 0 ? 0.0f : bmi;
    }

    @Override
    public Float calculateLBM(Float weight, Float bodyFat) {
        return weight * (1 - bodyFat / 100);
    }

    @Override
    // 1 - male, 2 - female
    public Float calculateBF(Integer gender, Integer height, Float waist, Float neck, Float hip) {
        float bf;
        if (gender == 1) {
            bf =  86.010f * (float) Math.log10(waist - neck) - 70.041f * (float) Math.log10(height) + 36.76f;
        } else {
            bf = 163.205f * (float) Math.log10(waist + hip - neck) - 97.684f * (float) Math.log10(height) - 78.387f;
        }
        return bf < 0 || bf > 100 ? 0.0f : bf;
    }
}
