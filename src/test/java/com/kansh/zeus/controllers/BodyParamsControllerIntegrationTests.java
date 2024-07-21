package com.kansh.zeus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.JsonObject;
import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.config.ValidateToken;
import com.kansh.zeus.domain.dto.body_params.BodyParamsDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.body_params.BodyParamsEntity;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.BodyParamService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Slf4j
public class BodyParamsControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValidateToken validateToken;

    @MockBean
    private BodyParamService bodyParamService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Mapper<BodyParamsEntity, BodyParamsDto> bodyParamsMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private UserTokenDto validUserToken;
    private BodyParamsDto bodyParamsDtoA1;
    private BodyParamsEntity bodyParamsEntityA1;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        bodyParamsDtoA1 = TestDataUtil.createTestBodyParamsDtoA1();
        bodyParamsEntityA1 = TestDataUtil.createTestBodyParamsEntityA1();
        validUserToken = TestDataUtil.createTestUserTokenA();
    }

    @Test
    public void testAddBodyParamsWithValidToken() throws Exception {
        when(validateToken.validateToken("Bearer validToken")).thenReturn(validUserToken);
        when(userRepository.findById(validUserToken.getId())).thenReturn(Optional.of(bodyParamsEntityA1.getUser()));
        when(bodyParamsMapper.mapFrom(any(BodyParamsDto.class))).thenReturn(bodyParamsEntityA1);
        when(bodyParamService.saveBodyParams(any(BodyParamsEntity.class))).thenReturn(bodyParamsEntityA1);
        when(bodyParamsMapper.mapTo(any(BodyParamsEntity.class))).thenReturn(bodyParamsDtoA1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bodyParams")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bodyParamsDtoA1)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddBodyParamsWithInvalidToken() throws Exception {
        when(validateToken.validateToken("Bearer invalidToken")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bodyParams")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bodyParamsDtoA1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllBodyParamsForUserWithValidToken() throws Exception {
        when(validateToken.validateToken("Bearer validToken")).thenReturn(validUserToken);
        when(bodyParamService.findAllBodyParamsForUser(anyString())).thenReturn(List.of(bodyParamsEntityA1));
        when(bodyParamsMapper.mapTo(any(BodyParamsEntity.class))).thenReturn(bodyParamsDtoA1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bodyParams/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllBodyParamsForUserWithInvalidToken() throws Exception {
        when(validateToken.validateToken("Bearer invalidToken")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bodyParams/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetBodyParamsForUserWithValidToken() throws Exception {
        when(validateToken.validateToken("Bearer validToken")).thenReturn(validUserToken);
        when(bodyParamService.findBodyParams(validUserToken.getId(), 1L)).thenReturn(Optional.of(bodyParamsEntityA1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bodyParams/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBodyParamsForUserWithInvalidToken() throws Exception {
        when(validateToken.validateToken("Bearer invalidToken")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/bodyParams/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateBodyParamsForUserWithValidToken() throws Exception {
        when(validateToken.validateToken("Bearer validToken")).thenReturn(validUserToken);
        when(bodyParamService.isExists(anyLong())).thenReturn(true);
        when(userRepository.findById(validUserToken.getId())).thenReturn(Optional.of(bodyParamsEntityA1.getUser()));
        when(bodyParamsMapper.mapFrom(any(BodyParamsDto.class))).thenReturn(bodyParamsEntityA1);
        when(bodyParamService.saveBodyParams(any(BodyParamsEntity.class))).thenReturn(bodyParamsEntityA1);
        when(bodyParamsMapper.mapTo(any(BodyParamsEntity.class))).thenReturn(bodyParamsDtoA1);

        JsonObject response = new JsonObject();
        response.addProperty("bodyParams", bodyParamsDtoA1.toString());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/bodyParams/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bodyParamsDtoA1)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateBodyParamsForUserWithInvalidToken() throws Exception {
        when(validateToken.validateToken("Bearer invalidToken")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/bodyParams/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bodyParamsDtoA1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteBodyParamsForUserWithValidToken() throws Exception {
        when(validateToken.validateToken("Bearer validToken")).thenReturn(validUserToken);
        when(bodyParamService.isExists(anyLong())).thenReturn(true);
        doNothing().when(bodyParamService).deleteBodyParams(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bodyParams/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteBodyParamsForUserWithInvalidToken() throws Exception {
        when(validateToken.validateToken("Bearer invalidToken")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/bodyParams/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
