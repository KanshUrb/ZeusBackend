package com.kansh.zeus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.config.ValidateToken;
import com.kansh.zeus.domain.dto.users.UserDto;
import com.kansh.zeus.domain.dto.users.UserInputDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    private Mapper<UsersEntity, UserDto> userMapper;

    private UserTokenDto validUserToken;
    private UserDto userDto;
    private UsersEntity usersEntity;
    private UserInputDto userInputDto;

    @Qualifier("objectMapper")
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        userDto = TestDataUtil.createTestUserDtoA();
        userInputDto = TestDataUtil.createTestUserInputDtoA();
        usersEntity = TestDataUtil.createTestUserEntityA();
        validUserToken = TestDataUtil.createTestUserTokenA();
    }

    @Test
    public void testCheckLoginWithInvalidToken() throws Exception {
        when(validateToken.validateToken("Bearer invalidToken")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void tesLoginWhenUserExists() throws Exception {
        String jsonContent = objectMapper.writeValueAsString(userInputDto);
        when(validateToken.validateToken("Bearer validToken")).thenReturn(validUserToken);
        when(userService.login(validUserToken, userInputDto)).thenReturn(usersEntity);
        when(userMapper.mapTo(usersEntity)).thenReturn(userDto);

        String expectedResponse = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void tesLoginWhenUserDontExist() throws Exception {
        String jsonContent = objectMapper.writeValueAsString(userInputDto);
        when(validateToken.validateToken("Bearer validToken")).thenReturn(validUserToken);
        when(userService.login(validUserToken, userInputDto)).thenReturn(usersEntity);
        when(userMapper.mapTo(usersEntity)).thenReturn(userDto);

        String expectedResponse = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(result -> log.info(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void testCheckIfUserExists() throws Exception {
        when(validateToken.validateToken("Bearer validToken")).thenReturn(validUserToken);
        when(userService.checkIfUserExists(validUserToken.getId())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testCheckIfUserDoesNotExist() throws Exception {
        when(validateToken.validateToken("Bearer validToken")).thenReturn(validUserToken);
        when(userService.checkIfUserExists(validUserToken.getId())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }



}
