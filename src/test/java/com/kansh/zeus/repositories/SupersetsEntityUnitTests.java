package com.kansh.zeus.repositories;

import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.domain.entities.exercises.UserSupersetsEntity;
import com.kansh.zeus.repositories.exercises.SupersetRepository;
import com.kansh.zeus.repositories.exercises.UserSupersetRepository;
import com.kansh.zeus.repositories.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SupersetsEntityUnitTests {

    @Autowired
    private SupersetRepository supersetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSupersetRepository userSupersetRepository;

    @Test
    void testFindAllSupersetsSummariesAvailableForUser() {
        // given
        UsersEntity user = new UsersEntity();
        user.setId("user123");
        userRepository.save(user);

        SupersetsEntity superset = new SupersetsEntity();
        superset.setName("Superset 1");
        superset.setRate(5.0F);
        superset.setCreatedBy(user);
        supersetRepository.save(superset);

        UserSupersetsEntity userSuperset = new UserSupersetsEntity();
        //userSuperset.setUser(user);
        userSuperset.setSuperset(superset);
        userSupersetRepository.save(userSuperset);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Object[]> result = supersetRepository.findAllSupersetsSummariesAvailableForUser("user123", pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        Object[] supersetSummary = result.getContent().get(0);
        assertThat(supersetSummary[1]).isEqualTo("Superset 1");
        assertThat(supersetSummary[2]).isEqualTo(5.0F);
    }

    @Test
    void testFindSupersetByUserAndId() {
        // given
        UsersEntity user = new UsersEntity();
        user.setId("user123");
        userRepository.save(user);

        SupersetsEntity superset = new SupersetsEntity();
        superset.setName("Superset 1");
        superset.setRate(5.0F);
        superset.setCreatedBy(user);
        supersetRepository.save(superset);

        UserSupersetsEntity userSuperset = new UserSupersetsEntity();
        //userSuperset.setUser(user);
        userSuperset.setSuperset(superset);
        userSupersetRepository.save(userSuperset);

        // Save all changes to the database
        userRepository.flush();
        supersetRepository.flush();
        userSupersetRepository.flush();

        // when
        Optional<SupersetsEntity> result = supersetRepository.findSupersetByUserAndId("user123", superset.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Superset 1");
    }

    @Test
    @Transactional
    void testDeleteByUserAndId() {
        // given
        UsersEntity user = new UsersEntity();
        user.setId("user123");
        userRepository.save(user);

        SupersetsEntity superset = new SupersetsEntity();
        superset.setName("Superset 1");
        superset.setRate(5.0F);
        superset.setCreatedBy(user);
        supersetRepository.save(superset);

        // when
        supersetRepository.deleteByIdAndCreatedBy_Id(superset.getId(), "user123");

        // then
        Optional<SupersetsEntity> result = supersetRepository.findById(superset.getId());
        assertThat(result).isNotPresent();
    }

    @Test
    void testUser2AccessesUser1SharedSuperset() {
        // given
        UsersEntity user1 = new UsersEntity();
        user1.setId("user1");
        userRepository.save(user1);

        UsersEntity user2 = new UsersEntity();
        user2.setId("user2");
        userRepository.save(user2);

        SupersetsEntity superset = new SupersetsEntity();
        superset.setName("Superset 1");
        superset.setRate(5.0F);
        superset.setCreatedBy(user1);
        supersetRepository.save(superset);

        UserSupersetsEntity userSuperset = new UserSupersetsEntity();
        //userSuperset.setUser(user1);
        userSuperset.setSuperset(superset);
        userSupersetRepository.save(userSuperset);

        UserSupersetsEntity sharedSuperset = new UserSupersetsEntity();
        //sharedSuperset.setUser(user1);
        sharedSuperset.setSuperset(superset);
        sharedSuperset.setSharedWith(user2); // user1 udostępnia superset user2
        userSupersetRepository.save(sharedSuperset);

        // Save all changes to the database
        userRepository.flush();
        supersetRepository.flush();
        userSupersetRepository.flush();

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<Object[]> result = supersetRepository.findAllSupersetsSummariesAvailableForUser("user2", pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        Object[] supersetSummary = result.getContent().get(0);
        assertThat(supersetSummary[1]).isEqualTo("Superset 1");
        assertThat(supersetSummary[2]).isEqualTo(5.0F);
    }
}
