package com.kansh.zeus.repositories;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.domain.entities.body_params.BodyParamsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repositories.body_params.BodyParamRepository;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.impl.BodyParamServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BodyParamsEntityUnitTests {

    @Autowired
    private BodyParamRepository bodyParamRepository;

    @Autowired
    private UserRepository userRepository;

    private BodyParamServiceImpl bodyParamsService;

    BodyParamsEntity bodyParamsEntityA = TestDataUtil.createTestBodyParamsEntityA1();
    BodyParamsEntity bodyParamsEntityB = TestDataUtil.createTestBodyParamsEntityB1();

    @BeforeEach
    public void setUp() {
        bodyParamsService = new BodyParamServiceImpl(bodyParamRepository);
        UsersEntity usersEntityA = TestDataUtil.createTestUserEntityA();
        UsersEntity usersEntityB = TestDataUtil.createTestUserEntityB();
        userRepository.save(usersEntityA);
        userRepository.save(usersEntityB);
    }

    @Test
    public void testIsExists() {
        BodyParamsEntity bodyParamsEntity = TestDataUtil.createTestBodyParamsEntityA1();
        bodyParamRepository.save(bodyParamsEntity);

        assertTrue(bodyParamsService.isExists(bodyParamsEntity.getId()));
    }

    @Test
    public void testFindBodyParams() {
        BodyParamsEntity bodyParamsEntity = TestDataUtil.createTestBodyParamsEntityA1();
        bodyParamRepository.save(bodyParamsEntity);

        Optional<BodyParamsEntity> result = bodyParamsService.findBodyParams(bodyParamsEntity.getUser().getId(), bodyParamsEntity.getId());
        assertTrue(result.isPresent());
        assertEquals(bodyParamsEntity, result.get());
    }

    @Test
    public void testSaveBodyParams() {
        BodyParamsEntity bodyParamsEntity = TestDataUtil.createTestBodyParamsEntityA1();
        BodyParamsEntity result = bodyParamsService.saveBodyParams(bodyParamsEntity);

        assertEquals(bodyParamsEntity, result);
    }

    @Test
    public void testDeleteBodyParams() {
        BodyParamsEntity bodyParamsEntity = TestDataUtil.createTestBodyParamsEntityA1();
        bodyParamRepository.save(bodyParamsEntity);

        assertDoesNotThrow(() -> bodyParamsService.deleteBodyParams(bodyParamsEntity.getId()));
        assertFalse(bodyParamRepository.existsById(bodyParamsEntity.getId()));
    }

    @Test
    public void testFindAllBodyParamsForUser() {
        List<BodyParamsEntity> bodyParamsList = Arrays.asList(
                TestDataUtil.createTestBodyParamsEntityA1(),
                TestDataUtil.createTestBodyParamsEntityA2(),
                TestDataUtil.createTestBodyParamsEntityA3()
        );
        log.info("bodyParamsList: {}", bodyParamsList);
        bodyParamRepository.saveAll(bodyParamsList);

        List<BodyParamsEntity> result = bodyParamsService.findAllBodyParamsForUser("U83rEkVPo4XJj87dm2NnSfXlS123");
        log.info("result: {}", result);
        assertEquals(3, result.size());
        assertEquals(bodyParamsList, result);
    }

    @Test
    public void testCalculateBMI() {
        Float weight = bodyParamsEntityA.getWeight();
        Integer height = bodyParamsEntityA.getHeight();
        Float expectedBMI = bodyParamsEntityA.getBmi();

        Float result = bodyParamsService.calculateBMI(height, weight);
        assertEquals(expectedBMI, result, 0.1);
    }

    @Test
    public void testCalculateLBM() {
        Float weight = bodyParamsEntityA.getWeight();
        Float bodyFat = bodyParamsEntityA.getBf();
        Float expectedLBM = bodyParamsEntityA.getLbm();

        Float result = bodyParamsService.calculateLBM(weight, bodyFat);
        assertEquals(expectedLBM, result, 0.1);
    }

    @Test
    public void testCalculateBFForMale() {
        Integer gender = 1;
        Integer height = bodyParamsEntityA.getHeight();
        Float waist = bodyParamsEntityA.getWaist();
        Float neck = bodyParamsEntityA.getNeck();
        Float expectedBF = bodyParamsEntityA.getBf();

        Float result = bodyParamsService.calculateBF(gender, height, waist, neck, null);
        assertEquals(expectedBF, result, 0.1);
    }

    @Test
    public void testCalculateBFForFemale() {
        Integer gender = 2;
        Integer height = bodyParamsEntityB.getHeight();
        Float waist = bodyParamsEntityB.getWaist();
        Float neck = bodyParamsEntityB.getNeck();
        Float hip = bodyParamsEntityB.getHip();
        Float expectedBF = bodyParamsEntityB.getBf();

        Float result = bodyParamsService.calculateBF(gender, height, waist, neck, hip);
        assertEquals(expectedBF, result, 0.1);
    }

}
