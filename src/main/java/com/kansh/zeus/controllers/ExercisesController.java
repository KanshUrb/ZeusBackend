package com.kansh.zeus.controllers;

import com.kansh.zeus.config.ValidateToken;
import com.kansh.zeus.domain.dto.users.UsersDto;
import com.kansh.zeus.domain.dto.exercises.*;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.services.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class ExercisesController {

    private final ExerciseService exerciseService;

    private final Mapper<ExercisesEntity, ExercisesDto> exercisesMapper;

    private final Mapper<SupersetsEntity, SupersetsDto> supersetsMapper;

    private final Mapper<UsersEntity, UsersDto> usersMapper;

    private final ValidateToken validateToken;

    @Autowired
    public ExercisesController(ExerciseService exerciseService,
                               ValidateToken validateToken,
                               Mapper<ExercisesEntity, ExercisesDto> exercisesMapper,
                               Mapper<SupersetsEntity, SupersetsDto> supersetsMapper,
                               Mapper<UsersEntity, UsersDto> usersMapper) {
        this.exerciseService = exerciseService;
        this.validateToken = validateToken;
        this.exercisesMapper = exercisesMapper;
        this.supersetsMapper = supersetsMapper;
        this.usersMapper = usersMapper;
    }

    @GetMapping("/exercises/summaries/{userId}/")
    public ResponseEntity<PageDto<ExercisesSummaryDto>> getExercisesSummaryAvailableForUser(@RequestHeader("Authorization") String authorizationHeader,
                                                                       @PathVariable String userId,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        log.info("ExercisesController::getExercisesSummaryAvailableForUser START");
        log.debug("ExercisesController::getExercisesSummaryAvailableForUser userId = {}, page = {}, size = {}", userId, page, size);

        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::getExercisesSummaryAvailableForUser ERROR : Invalid authorization header!");
            PageDto<ExercisesSummaryDto> emptyResponse = new PageDto<>(Collections.emptyList(), page, size, 0, 0);
            return ResponseEntity.badRequest().body(emptyResponse);
        }

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Object[]> exercisesSummaryEntity = exerciseService.getExercisesSummariesAvailableForUser(userId, pageable);
            List<ExercisesSummaryDto> exercisesSummaryDto = exercisesSummaryEntity.stream()
                    .map(result -> ExercisesSummaryDto.builder()
                            .id((Long) result[0])
                            .name((String) result[1])
                            .build())
                    .toList();

            PageDto<ExercisesSummaryDto> response = PageDto.<ExercisesSummaryDto>builder()
                    .result(exercisesSummaryDto)
                    .pageNumber(exercisesSummaryEntity.getNumber())
                    .pageSize(exercisesSummaryEntity.getSize())
                    .totalPages(exercisesSummaryEntity.getTotalPages())
                    .totalElements(exercisesSummaryEntity.getTotalElements())
                    .build();

            log.info("ExercisesController::getExercisesSummaryAvailableForUser STOP response = {}", response);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("ExercisesController::getExercisesSummaryAvailableForUser ERROR", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/exercises/{exerciseId}/{userId}")
    public ResponseEntity<ExercisesDto> getExercisesByUserAndId(@RequestHeader("Authorization") String authorizationHeader,
                                                @PathVariable Long exerciseId,
                                                @PathVariable String userId) {
        log.info("ExercisesController::getExercisesByUserAndId START, exerciseId = {}, userId = {}", exerciseId, userId);

        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::getExercisesByUserAndId ERROR : Invalid authorization header!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<ExercisesEntity> exercise = exerciseService.getExerciseByUserAndId(userId, exerciseId);
        if (exercise.isPresent()) {
            ExercisesEntity exercisesEntity = exercise.get();
            log.info("ExercisesController::getExerciseByUserAndId STOP exercise = {}",exercisesEntity);
            return new ResponseEntity<>(exercisesMapper.mapTo(exercisesEntity), HttpStatus.OK);
        } else {
            log.info("ExercisesController::getExerciseByUserAndId ERROR Exercise not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("exercises/exercise/")
    public ResponseEntity<ExercisesDto> createExercise(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody CreateExerciseWrapper wrapper) {
        log.info("ExercisesController::createExercise START");

        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::createExercise ERROR : Invalid authorization header!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        ExercisesDto exercise = wrapper.getExercise();
        UsersDto user = wrapper.getUser();
        List<String> sharedWith = wrapper.getSharedWith();
        log.info("ExercisesController::addExercise exercise = {}, user = {}, sharedWith = {}", exercise.toString(), user.toString(), sharedWith);

        try {
            ExercisesEntity savedExercise = exerciseService.createExercise(exercisesMapper.mapFrom(exercise), usersMapper.mapFrom(user), sharedWith);
            log.info("ExercisesController::createExercise STOP exercise = {}", savedExercise);
            return new ResponseEntity<>(exercisesMapper.mapTo(savedExercise), HttpStatus.OK);
        } catch (Exception e) {
            log.info("ExercisesController::createExercise ERROR error = {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("exercises/exercise/")
    public ResponseEntity<Void> deleteExercise(@RequestHeader("Authorization") String authorizationHeader,
                                               @RequestBody ExercisesDto exercisesDto) {
        log.info("ExercisesController::deleteExercise START exerciseDto = {}", exercisesDto);

        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::deleteExercise ERROR : Invalid authorization header!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        ExercisesEntity exercisesEntity = exercisesMapper.mapFrom(exercisesDto);
        exerciseService.deleteExercise(exercisesEntity.getId(), exercisesEntity.getCreatedBy().getId());

        log.info("ExercisesController::deleteExercise STOP");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("exercises/exercise/")
    public ResponseEntity<ExercisesDto> updateExercise(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody ExercisesDto exercisesDto) {
        log.info("ExercisesController::updateExercise START, exerciseDto = {}", exercisesDto);

        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::updateExercise ERROR : Invalid authorization header!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        ExercisesEntity exercisesEntity = exerciseService.updateExercise(exercisesMapper.mapFrom(exercisesDto));
        log.info("ExercisesController::updateExercise STOP, exerciseEntity = {}", exercisesEntity.toString());

        return new ResponseEntity<>(exercisesMapper.mapTo(exercisesEntity), HttpStatus.OK);
    }

    @GetMapping("exercises/rate/{exerciseId}/{rate}/")
    public ResponseEntity<Void> rateExercise(@RequestHeader("Authorization") String authorizationHeader,
                                                     @PathVariable Long exerciseId,
                                                     @PathVariable Integer rate) {
        log.info("ExercisesController::rateExercise START, exerciseId = {}, rate = {}", exerciseId, rate);

        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::rateExercise ERROR : Invalid authorization header!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        exerciseService.rateExercise(exerciseId, rate);
        log.info("ExercisesController::rateExercise STOP");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/supersets/summaries/{userId}/")
    public ResponseEntity<PageDto<SupersetsSummaryDto>> getSupersetsSummaryAvailableForUser(@RequestHeader("Authorization") String authorizationHeader,
                                                                                            @PathVariable String userId,
                                                                                            @RequestParam(defaultValue = "0") int page,
                                                                                            @RequestParam(defaultValue = "10") int size) {
        log.info("ExercisesController::getSupersetsSummaryAvailableForUser START");
        log.debug("ExercisesController::getSupersetsSummaryAvailableForUser userId = {}, page = {}, size = {}", userId, page, size);

        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::getSupersetsSummaryAvailableForUser ERROR : Invalid authorization header!");
            PageDto<SupersetsSummaryDto> emptyResponse = new PageDto<>(Collections.emptyList(), page, size, 0, 0);
            return ResponseEntity.badRequest().body(emptyResponse);
        }

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Object[]> supersetsSummaryEntity = exerciseService.getSupersetsSummariesAvailableForUser(userId, pageable);
            List<SupersetsSummaryDto> supersetsSummaryDto = supersetsSummaryEntity.stream()
                    .map(result -> SupersetsSummaryDto.builder()
                            .id((Long) result[0])
                            .name((String) result[1])
                            .build())
                    .toList();

            PageDto<SupersetsSummaryDto> response = PageDto.<SupersetsSummaryDto>builder()
                    .result(supersetsSummaryDto)
                    .pageNumber(supersetsSummaryEntity.getNumber())
                    .pageSize(supersetsSummaryEntity.getSize())
                    .totalPages(supersetsSummaryEntity.getTotalPages())
                    .totalElements(supersetsSummaryEntity.getTotalElements())
                    .build();

            log.info("ExercisesController::getSupersetsSummaryAvailableForUser STOP response = {}", response);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("ExercisesController::getSupersetsSummaryAvailableForUser ERROR", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/supersets/{supersetId}/{userId}")
    public ResponseEntity<SupersetsDto> getSupersetByUserAndId(@RequestHeader("Authorization") String authorizationHeader,
                                                                @PathVariable Long supersetId,
                                                                @PathVariable String userId) {
        log.info("ExercisesController::getSupersetByUserAndId START, exerciseId = {}, userId = {}", supersetId, userId);

        if (validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::getSupersetByUserAndId ERROR : Invalid authorization header!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<SupersetsEntity> superset = exerciseService.getSupersetByUserAndId(userId, supersetId);
        if (superset.isPresent()) {
            SupersetsEntity supersetEntity = superset.get();
            log.info("ExercisesController::getSupersetByUserAndId STOP exercise = {}", supersetEntity);
            return new ResponseEntity<>(supersetsMapper.mapTo(supersetEntity), HttpStatus.OK);
        } else {
            log.info("ExercisesController::getSupersetByUserAndId ERROR Exercise not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("supersets/superset/")
    public ResponseEntity<SupersetsDto> createSuperset(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody CreateSupersetWrapper wrapper) {
        log.info("ExercisesController::createSuperset START");

        if (validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::createSuperset ERROR : Invalid authorization header!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        SupersetsDto superset = wrapper.getSuperset();
        UsersDto user = wrapper.getUser();
        List<String> sharedWith = wrapper.getSharedWith();
        log.info("ExercisesController::addSuperset superset = {}, user = {}, sharedWith = {}", superset.toString(), user.toString(), sharedWith);

        try {
            SupersetsEntity savedSuperset = exerciseService.createSuperset(supersetsMapper.mapFrom(superset), usersMapper.mapFrom(user), sharedWith);
            log.info("ExercisesController::createSuperset STOP superset = {}", savedSuperset);
            return new ResponseEntity<>(supersetsMapper.mapTo(savedSuperset), HttpStatus.OK);
        } catch (Exception e) {
            log.info("ExercisesController::createSuperset ERROR error = {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("supersets/superset/")
    public ResponseEntity<Void> deleteSuperset(@RequestHeader("Authorization") String authorizationHeader,
                                               @RequestBody SupersetsDto supersetsDto) {
        log.info("ExercisesController::deleteSuperset START exerciseDto = {}", supersetsDto);

        if (validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::deleteSuperset ERROR : Invalid authorization header!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        SupersetsEntity supersetEntity = supersetsMapper.mapFrom(supersetsDto);
        exerciseService.deleteSuperset(supersetEntity.getId(), supersetEntity.getCreatedBy().getId());

        log.info("ExercisesController::deleteSuperset STOP");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("supersets/superset/")
    public ResponseEntity<SupersetsDto> updateSuperset(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody SupersetsDto supersetsDto) {
        log.info("ExercisesController::updateSuperset START, supersetDto = {}", supersetsDto);

        if (validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::updateSuperset ERROR : Invalid authorization header!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        SupersetsEntity supersetEntity = exerciseService.updateSuperset(supersetsMapper.mapFrom(supersetsDto));
        log.info("ExercisesController::updateSuperset STOP, exerciseEntity = {}", supersetEntity.toString());

        return new ResponseEntity<>(supersetsMapper.mapTo(supersetEntity), HttpStatus.OK);
    }

    @GetMapping("supersets/rate/{supersetId}/{rate}/")
    public ResponseEntity<Void> rateSuperset(@RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable Long supersetId,
                                             @PathVariable Integer rate) {
        log.info("ExercisesController::rateSuperset START, supersetId = {}, rate = {}", supersetId, rate);

        if (validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::rateSuperset ERROR : Invalid authorization header!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        exerciseService.rateSuperset(supersetId, rate);
        log.info("ExercisesController::rateSuperset STOP");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/exercises/technical/")
    public ResponseEntity<ExercisesDto> addExercise(@RequestHeader("Authorization") String authorizationHeader,
                                                    @RequestBody ExercisesDto exercisesDto) {
        log.info("ExercisesController::addExercise START");
        log.debug("ExercisesController::addExercise exercise = {}", exercisesDto.toString());

        if (validateToken.validateToken(authorizationHeader) == null) {
            log.error("ExercisesController::addExercise ERROR : Invalid authorization header!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        ExercisesEntity exercisesEntity = exercisesMapper.mapFrom(exercisesDto);
        exercisesEntity = exerciseService.addExerciseTechnical(exercisesEntity);

        log.info("ExercisesController::addExercise STOP");
        return new ResponseEntity<>(exercisesMapper.mapTo(exercisesEntity), HttpStatus.OK);
    }

}
