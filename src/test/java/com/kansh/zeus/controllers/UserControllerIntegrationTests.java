package com.kansh.zeus.controllers;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.config.ValidateToken;
import com.kansh.zeus.domain.dto.users.UsersDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Slf4j
public class UserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValidateToken validateToken;

    @MockBean
    private UserService userService;

    @MockBean
    private Mapper<UsersEntity, UsersDto> userMapper;

    private UserTokenDto validUserToken;
    private UsersDto usersDto;
    private UsersEntity usersEntity;

    @BeforeEach
    public void setUp() {
        usersDto = TestDataUtil.createTestUserDtoA();
        usersEntity = TestDataUtil.createTestUserEntityA();
        validUserToken = TestDataUtil.createTestUserTokenA();
    }

    @Test
    public void testCheckIfUserExistsValidTokenAndUserExists() throws Exception {
        when(validateToken.validateToken("Bearer validToken")).thenReturn(validUserToken);
        when(userService.checkIfUserExists(validUserToken.getId())).thenReturn(true);
        when(userService.loginUser(validUserToken.getId())).thenReturn(usersEntity);
        when(userMapper.mapTo(usersEntity)).thenReturn(usersDto);

        String jsonContent = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"gender\": 1 }";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void testCheckIfUserExistsWithValidTokenAndUserDoesNotExist() throws Exception {
        when(validateToken.validateToken("Bearer validToken")).thenReturn(validUserToken);
        when(userService.checkIfUserExists(validUserToken.getId())).thenReturn(false);
        when(userService.registerUser(validUserToken, "John", "Doe", 1)).thenReturn(usersEntity);
        when(userMapper.mapTo(usersEntity)).thenReturn(usersDto);

        String jsonContent = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"gender\": 1 }";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    public void testCheckIfUserExistsWithInvalidToken() throws Exception {
        when(validateToken.validateToken("Bearer invalidToken")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
