package com.kansh.zeus.controllers;

import com.kansh.zeus.config.ValidateToken;
import com.kansh.zeus.domain.dto.trainings.TrainingInputWrapperDto;
import com.kansh.zeus.domain.dto.trainings.TrainingItemDto;
import com.kansh.zeus.domain.dto.trainings.TrainingSummaryDto;
import com.kansh.zeus.domain.dto.trainings.TrainingsDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mappers.impl.TrainingItemDtoConverter;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.TrainingService;
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
public class TrainingController {

    private final ValidateToken validateToken;
    private final UserRepository userRepository;
    private final TrainingService trainingService;
    private final TrainingItemDtoConverter trainingItemConverter;

    @Autowired
    public TrainingController(ValidateToken validateToken,
                              UserRepository userRepository,
                              TrainingService trainingService,
                              TrainingItemDtoConverter trainingItemConverter) {
        this.validateToken = validateToken;
        this.userRepository = userRepository;
        this.trainingService = trainingService;
        this.trainingItemConverter = trainingItemConverter;
    }

    @PostMapping("/training")
    public ResponseEntity<TrainingsDto> createTraining(@RequestHeader("Authorization") String authorization,
                                                       @RequestBody TrainingInputWrapperDto trainingWrapper) {
        log.info("TrainingController::createTraining START, trainingWrapper = {}", trainingWrapper);

        UserTokenDto userToken = validateToken.validateToken(authorization);
        if (isNull(userToken)) {
            log.error("TrainingController::createTraining ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if(isNull(user)) {
                log.error("TrainingController::createTraining ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<TrainingItemDto> trainingItems = trainingItemConverter.convert(trainingWrapper.getTrainingItems(), user.getId());
            TrainingsDto training = trainingService.createTraining(user, trainingWrapper.getName(), trainingWrapper.getNote(), trainingItems, trainingWrapper.getSharedWith());

            if(isNull(training)) {
                log.error("TrainingController::createTraining ERROR : Training not created!");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("TrainingController::createTraining STOP training = {}", training);
            return new ResponseEntity<>(training, HttpStatus.CREATED);

        } catch(Exception e) {
            log.error("TrainingController::createTraining ERROR : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/training/{trainingId}")
    public ResponseEntity<Void> deleteTraining(@RequestHeader("Authorization") String authorization,
                                               @PathVariable Long trainingId) {
        log.info("TrainingController::deleteTraining START");

        UserTokenDto userToken = validateToken.validateToken(authorization);
        if (isNull(userToken)) {
            log.error("TrainingController::deleteTraining ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("ExercisesController::createSuperset ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            trainingService.deleteTraining(trainingId, user.getId());

            log.info("TrainingController::deleteTraining STOP");
            return new ResponseEntity<>(HttpStatus.OK);

        } catch(Exception e) {
            log.error("TrainingController::deleteTraining ERROR : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //todo: implement updateTraining
    /*@PutMapping("/training/{trainingId}")
    public ResponseEntity<TrainingsDto> updateTraining(@RequestHeader("Authorization") String authorization,
                                                       @PathVariable Long trainingId,
                                                       @RequestBody TrainingInputWrapperDto trainingWrapper) {
        log.info("TrainingController::updateTraining START");

        UserTokenDto userToken = validateToken.validateToken(authorization);
        if (isNull(userToken)) {
            log.error("TrainingController::updateTraining ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if(isNull(user)) {
                log.error("TrainingController::updateTraining ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<TrainingItemDto> trainingItems = trainingItemConverter.convert(trainingWrapper.getTrainingItems(), user.getId());
            TrainingsDto training = trainingService.updateTraining(user, trainingId, trainingWrapper.getName(), trainingWrapper.getNote(), trainingItems, trainingWrapper.getSharedWith());

            if(isNull(training)) {
                log.error("TrainingController::updateTraining ERROR : Training not updated!");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("TrainingController::updateTraining STOP training = {}", training);
            return new ResponseEntity<>(training, HttpStatus.OK);

        } catch(Exception e) {
            log.error("TrainingController::updateTraining ERROR : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @GetMapping("/training/{trainingId}")
    public ResponseEntity<TrainingsDto> getTrainingById(@RequestHeader("Authorization") String authorization,
                                                        @PathVariable Long trainingId) {
        log.info("TrainingController::getTrainingById START, {}", trainingId);

        UserTokenDto userToken = validateToken.validateToken(authorization);
        if (isNull(userToken)) {
            log.error("TrainingController::getTrainingById ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("TrainingController:getTrainingById ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Optional<TrainingsDto> training = trainingService.getTrainingById(user.getId(), trainingId);

            if(training.isEmpty()) {
                log.error("TrainingController::getTrainingById ERROR : Training not found!");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            log.info("TrainingController::getTrainingById STOP training = {}", training);
            return new ResponseEntity<>(training.get(), HttpStatus.OK);

        } catch(Exception e) {
            log.error("TrainingController::getTrainingById ERROR : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/training/summaries")
    public ResponseEntity<List<TrainingSummaryDto>> getTrainingsSummaryAvailableForUser(@RequestHeader("Authorization") String authorization) {
        log.info("TrainingController::getTrainingsSummaryAvailableForUser START");

        UserTokenDto userToken = validateToken.validateToken(authorization);
        if (isNull(userToken)) {
            log.error("TrainingController::getTrainingsSummaryAvailableForUser ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("ExercisesController::createSuperset ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            List<TrainingSummaryDto> trainingSummary = trainingService.getTrainingsSummariesAvailableForUser(user.getId());

            if(isNull(trainingSummary)) {
                log.error("TrainingController::getTrainingsSummaryAvailableForUser ERROR : Training summary not found!");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            log.info("TrainingController::getTrainingsSummaryAvailableForUser STOP trainingSummary = {}", trainingSummary);
            return new ResponseEntity<>(trainingSummary, HttpStatus.OK);

        } catch(Exception e) {
            log.error("TrainingController::getTrainingsSummaryAvailableForUser ERROR : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
