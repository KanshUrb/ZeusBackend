package com.kansh.zeus.repositories;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repositories.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UsersEntityRepositoryUnitTests {

    private UserRepository userRepository;

    @Autowired
    public UsersEntityRepositoryUnitTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void testThatCheckIfUserCanBeFoundAndDoesNotExists() {
        String userId = "0000000000000000000000000000";
        Optional<UsersEntity> usersEntity = userRepository.findById(userId);
        assertThat(usersEntity).isEmpty();
    }

    @Test
    public void testThatCheckIfUserCanBeFoundAndExists() {
        UsersEntity usersEntity = TestDataUtil.createTestUserEntityA();
        userRepository.save(usersEntity);
        Optional<UsersEntity> savedUsersEntity = userRepository.findById(usersEntity.getId());
        assertThat(savedUsersEntity).isPresent();
        assertThat(savedUsersEntity.get()).isEqualTo(usersEntity);
    }

}
