package com.kansh.zeus.unit;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.domain.dto.users.UserInputDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repository.users.UserRepository;
import com.kansh.zeus.service.impl.UserServiceImpl;
import com.kansh.zeus.utils.HashGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HashGenerator hashGenerator;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckIfUserExists_UserExists() {

        UsersEntity testUser = TestDataUtil.createTestUserEntityA();
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        boolean exists = userService.checkIfUserExists(testUser.getId());

        assertThat(exists).isTrue();
        verify(userRepository, times(1)).findById(testUser.getId());
    }

    @Test
    public void testCheckIfUserExists_UserDoesNotExist() {

        String nonExistentUserId = "nonExistentUserId";
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        boolean exists = userService.checkIfUserExists(nonExistentUserId);

        assertThat(exists).isFalse();
        verify(userRepository, times(1)).findById(nonExistentUserId);
    }

    @Test
    public void testLogin_UserExists() {

        UsersEntity testUser = TestDataUtil.createTestUserEntityA();
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        UserTokenDto userToken = TestDataUtil.createTestUserTokenA();
        UserInputDto userInputDto = TestDataUtil.createTestUserInputDtoA();

        UsersEntity loggedInUser = userService.login(userToken, userInputDto);

        assertThat(loggedInUser).isEqualTo(testUser);
        verify(userRepository, times(2)).findById(testUser.getId());
    }

    @Test
    public void testLogin_UserDoesNotExist() {

        UsersEntity testUser = TestDataUtil.createTestUserEntityA();
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());
        when(hashGenerator.generateHash(anyString())).thenReturn("123456");
        when(hashGenerator.checkIfHashIsAvailable(anyString())).thenReturn(false);

        UserTokenDto userToken = TestDataUtil.createTestUserTokenA();
        UserInputDto userInputDto = TestDataUtil.createTestUserInputDtoA();

        UsersEntity registeredUser = new UsersEntity();
        when(userRepository.save(any(UsersEntity.class))).thenReturn(registeredUser);

        UsersEntity loggedInUser = userService.login(userToken, userInputDto);

        assertThat(loggedInUser).isEqualTo(registeredUser);
        verify(userRepository, times(1)).save(any(UsersEntity.class));
    }

    @Test
    public void testRegisterUser() {

        String generatedHash = "123456";
        when(hashGenerator.generateHash(anyString())).thenReturn(generatedHash);
        when(hashGenerator.checkIfHashIsAvailable(anyString())).thenReturn(false);

        UserTokenDto userToken = TestDataUtil.createTestUserTokenA();
        UserInputDto userInputDto = TestDataUtil.createTestUserInputDtoA();

        UsersEntity savedUser = TestDataUtil.createTestUserEntityA();
        savedUser.setHash(generatedHash);
        when(userRepository.save(any(UsersEntity.class))).thenReturn(savedUser);

        UsersEntity registeredUser = userService.registerUser(userToken, userInputDto);

        assertThat(registeredUser).isEqualTo(savedUser);
        verify(userRepository, times(1)).save(any(UsersEntity.class));
        verify(hashGenerator, times(1)).generateHash(anyString());
        verify(hashGenerator, times(1)).checkIfHashIsAvailable(generatedHash);
    }

    @Test
    public void testLoginUser_UserExists() {

        UsersEntity testUser = TestDataUtil.createTestUserEntityA();
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        UsersEntity loggedInUser = userService.loginUser(testUser.getId());

        assertThat(loggedInUser).isEqualTo(testUser);
        verify(userRepository, times(1)).findById(testUser.getId());
    }

    @Test
    public void testLoginUser_UserDoesNotExist() {

        String nonExistentUserId = "nonExistentUserId";
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> userService.loginUser(nonExistentUserId));

        assertThat(exception.getMessage()).isEqualTo("User not found with id: " + nonExistentUserId);
        verify(userRepository, times(1)).findById(nonExistentUserId);
    }
}
