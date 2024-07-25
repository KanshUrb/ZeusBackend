package com.kansh.zeus.controllers;

import com.kansh.zeus.config.ValidateToken;
import com.kansh.zeus.domain.dto.body_params.BodyParamsDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.body_params.BodyParamsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.BodyParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@RestController
@RequestMapping("/api")
public class BodyParamsController {

    private final ValidateToken validateToken;

    private final Mapper<BodyParamsEntity, BodyParamsDto> bodyParamsMapper;

    private final BodyParamService bodyParamService;

    private final UserRepository userRepository;

    @Autowired
    public BodyParamsController(Mapper<BodyParamsEntity, BodyParamsDto> bodyParamsMapper, ValidateToken validateToken, BodyParamService bodyParamService, UserRepository userRepository) {
        this.bodyParamsMapper = bodyParamsMapper;
        this.validateToken = validateToken;
        this.bodyParamService = bodyParamService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/bodyParams")
    public ResponseEntity<BodyParamsDto> addBodyParams(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody BodyParamsDto bodyParamsDto) {
        log.info("BodyPramsController::addBodyParams START");
        log.info("BodyPramsController::addBodyParams bodyParamsDto = {}", bodyParamsDto.toString());

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("BodyPramsController::addBodyParams ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        BodyParamsEntity savedBodyParamsEntity;
        try {
            BodyParamsEntity bodyParamsEntity = bodyParamsMapper.mapFrom(bodyParamsDto);
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("BodyPramsController::addBodyParams ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            bodyParamsEntity.setUser(user);
            log.info("BodyPramsController::addBodyParams bodyParamsEntity = {}", bodyParamsEntity);
            savedBodyParamsEntity = bodyParamService.saveBodyParams(bodyParamsEntity);
            if (isNull(savedBodyParamsEntity)) {
                log.info("BodyPramsController::addBodyParams STOP response = BAD_REQUEST");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("BodyPramsController::addBodyParams ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("BodyPramsController::addBodyParams STOP response = CREATED");
        return new ResponseEntity<>(bodyParamsMapper.mapTo(savedBodyParamsEntity), HttpStatus.CREATED);
    }

    @GetMapping(value = "/bodyParams")
    public ResponseEntity<List<BodyParamsDto>> getAllBodyParamsForUser(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("BodyPramsController::getAllBodyParamsForUser START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("BodyPramsController::getAllBodyParamsForUser ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            List<BodyParamsEntity> bodyParamsEntity = bodyParamService.findAllBodyParamsForUser(userToken.getId());
            List<BodyParamsDto> bodyParamsDto = bodyParamsEntity.stream()
                    .map(bodyParamsMapper::mapTo)
                    .toList();

            log.info("BodyPramsController::getAllBodyParamsForUser STOP bodyParamsDto = {}", bodyParamsDto);
            return new ResponseEntity<>(bodyParamsDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("BodyPramsController::getAllBodyParamsForUser ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/bodyParams/{bodyParamId}")
    public ResponseEntity<BodyParamsDto> getBodyParamsForUser(@RequestHeader("Authorization") String authorizationHeader,
                                                              @PathVariable Long bodyParamId) {
        log.info("BodyPramsController::getBodyParamsForUser START");
        log.info("BodyPramsController::getBodyParamsForUser bodyParamId = {}", bodyParamId);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("BodyPramsController::getBodyParamsForUser ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Optional<BodyParamsEntity> bodyParamsEntity = bodyParamService.findBodyParams(userToken.getId(), bodyParamId);
            if (bodyParamsEntity.isPresent()) {
                BodyParamsDto bodyParamDto = bodyParamsMapper.mapTo(bodyParamsEntity.get());
                log.info("BodyParamsController::getBodyParamsById STOP, response = {}", bodyParamDto);
                return new ResponseEntity<>(bodyParamDto, HttpStatus.OK);
            } else {
                log.warn("BodyParams not found for: userId = {}, bodyParamId = {}", userToken.getId(), bodyParamId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("BodyPramsController::getBodyParamsForUser ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/bodyParams/{bodyParamId}")
    public ResponseEntity<Void> updateBodyParamsForUser(@RequestHeader("Authorization") String authorizationHeader,
                                                        @PathVariable Long bodyParamId,
                                                        @RequestBody BodyParamsDto bodyParamsDto) {
        log.info("BodyPramsController::updateBodyParamsForUser START");
        log.info("BodyPramsController::updateBodyParamsForUser userId = {}, newBodyParams = {}", bodyParamId, bodyParamsDto.toString());
        log.info(authorizationHeader);
        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("BodyPramsController::updateBodyParamsForUser ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            if (!bodyParamService.isExists(bodyParamId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            bodyParamsDto.setId(bodyParamId);
            BodyParamsEntity bodyParamsEntity = bodyParamsMapper.mapFrom(bodyParamsDto);
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("BodyPramsController::updateBodyParams ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            bodyParamsEntity.setUser(user);
            BodyParamsEntity savedBodyParamsEntity = bodyParamService.saveBodyParams(bodyParamsEntity);
            if (isNull(savedBodyParamsEntity)) {
                log.info("BodyPramsController::updateBodyParamsForUser STOP response = BAD_REQUEST");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("BodyPramsController::updateBodyParamsForUser ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/bodyParams/{bodyParamId}")
    public ResponseEntity<Void> deleteBodyParamsForUser(@RequestHeader("Authorization") String authorizationHeader,
                                                        @PathVariable Long bodyParamId) {
        log.info("BodyPramsController::deleteBodyParamsForUser START");
        log.info("BodyPramsController::deleteBodyParamsForUser bodyParamId = {}", bodyParamId);

        if (validateToken.validateToken(authorizationHeader) == null) {
            log.error("BodyPramsController::deleteBodyParamsForUser ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            bodyParamService.deleteBodyParams(bodyParamId);
            log.info("BodyPramsController::deleteBodyParamsForUser STOP");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("BodyPramsController::getBodyParamsForUser ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
