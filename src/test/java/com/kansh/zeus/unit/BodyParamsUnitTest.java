package com.kansh.zeus.unit;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.domain.entities.body_params.BodyParamsEntity;
import com.kansh.zeus.repository.body_params.BodyParamRepository;
import com.kansh.zeus.service.impl.BodyParamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BodyParamsUnitTest {

    @Mock
    private BodyParamRepository bodyParamRepository;

    @InjectMocks
    private BodyParamServiceImpl bodyParamService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsExists() {
        Long bodyParamId = 1L;
        when(bodyParamRepository.existsById(bodyParamId)).thenReturn(true);

        assertTrue(bodyParamService.isExists(bodyParamId));
        verify(bodyParamRepository, times(1)).existsById(bodyParamId);
    }

    @Test
    public void testFindBodyParams() {
        BodyParamsEntity bodyParamsEntity = TestDataUtil.createTestBodyParamsEntityA1();
        when(bodyParamRepository.findByIdAndUserId(bodyParamsEntity.getId(), bodyParamsEntity.getUser().getId()))
                .thenReturn(Optional.of(bodyParamsEntity));

        Optional<BodyParamsEntity> result = bodyParamService.findBodyParams(bodyParamsEntity.getUser().getId(), bodyParamsEntity.getId());
        assertTrue(result.isPresent());
        assertEquals(bodyParamsEntity, result.get());
        verify(bodyParamRepository, times(1)).findByIdAndUserId(bodyParamsEntity.getId(), bodyParamsEntity.getUser().getId());
    }

    @Test
    public void testSaveBodyParams() {
        BodyParamsEntity bodyParamsEntity = TestDataUtil.createTestBodyParamsEntityA1();
        when(bodyParamRepository.save(any(BodyParamsEntity.class))).thenReturn(bodyParamsEntity);

        BodyParamsEntity result = bodyParamService.saveBodyParams(bodyParamsEntity);

        assertEquals(bodyParamsEntity, result);
        verify(bodyParamRepository, times(1)).save(any(BodyParamsEntity.class));
    }

    @Test
    public void testDeleteBodyParams() {
        Long bodyParamId = 1L;
        doNothing().when(bodyParamRepository).deleteById(bodyParamId);

        assertDoesNotThrow(() -> bodyParamService.deleteBodyParams(bodyParamId));
        verify(bodyParamRepository, times(1)).deleteById(bodyParamId);
    }

    @Test
    public void testFindAllBodyParamsForUser() {
        String userId = "U83rEkVPo4XJj87dm2NnSfXlS123";
        List<BodyParamsEntity> bodyParamsList = Arrays.asList(
                TestDataUtil.createTestBodyParamsEntityA1(),
                TestDataUtil.createTestBodyParamsEntityA2(),
                TestDataUtil.createTestBodyParamsEntityA3()
        );
        when(bodyParamRepository.findAllByUserIdOrderByIdDesc(userId)).thenReturn(bodyParamsList);

        List<BodyParamsEntity> result = bodyParamService.findAllBodyParamsForUser(userId);
        assertEquals(3, result.size());
        assertEquals(bodyParamsList, result);
        verify(bodyParamRepository, times(1)).findAllByUserIdOrderByIdDesc(userId);
    }

    @Test
    public void testCalculateBMI() {
        Float weight = 75.0f;
        Integer height = 180;
        float expectedBMI = 23.15f;

        Float result = bodyParamService.calculateBMI(height, weight);
        assertEquals(expectedBMI, result, 0.1);
    }

    @Test
    public void testCalculateLBM() {
        Float weight = 75.0f;
        Float bodyFat = 16.54f;
        float expectedLBM = 62.57f;

        Float result = bodyParamService.calculateLBM(weight, bodyFat);
        assertEquals(expectedLBM, result, 0.1);
    }

    @Test
    public void testCalculateBFForMale() {
        Integer gender = 1;
        Integer height = 180;
        Float waist = 80.0f;
        Float neck = 40.0f;
        float expectedBF = 16.54f;

        Float result = bodyParamService.calculateBF(gender, height, waist, neck, null);
        assertEquals(expectedBF, result, 0.1);
    }

    @Test
    public void testCalculateBFForFemale() {
        Integer gender = 2;
        Integer height = 165;
        Float waist = 75.0f;
        Float neck = 35.0f;
        Float hip = 95.0f;
        float expectedBF = 52.68f;

        Float result = bodyParamService.calculateBF(gender, height, waist, neck, hip);
        assertEquals(expectedBF, result, 0.1);
    }
}
