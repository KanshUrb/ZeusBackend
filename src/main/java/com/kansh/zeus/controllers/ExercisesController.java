package com.kansh.zeus.controllers;

import com.kansh.zeus.config.ValidateToken;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.dto.exercises.*;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@RestController
@RequestMapping("/api")
public class ExercisesController {

    private final ExerciseService exerciseService;

    private final UserRepository userRepository;

    private final Mapper<ExercisesEntity, ExercisesDto> exercisesMapper;

    private final Mapper<SupersetsEntity, SupersetsDto> supersetsMapper;

    private final ValidateToken validateToken;

    @Autowired
    public ExercisesController(ExerciseService exerciseService,
                               UserRepository userRepository,
                               ValidateToken validateToken,
                               Mapper<ExercisesEntity, ExercisesDto> exercisesMapper,
                               Mapper<SupersetsEntity, SupersetsDto> supersetsMapper) {
        this.exerciseService = exerciseService;
        this.userRepository = userRepository;
        this.validateToken = validateToken;
        this.exercisesMapper = exercisesMapper;
        this.supersetsMapper = supersetsMapper;
    }

    @GetMapping("/exercises/summaries")
    public ResponseEntity<PageDto<ExercisesSummaryDto>> getExercisesSummaryAvailableForUser(@RequestHeader("Authorization") String authorizationHeader,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        log.info("ExercisesController::getExercisesSummaryAvailableForUser START");
        log.debug("ExercisesController::getExercisesSummaryAvailableForUser page = {}, size = {}", page, size);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::getExercisesSummaryAvailableForUser ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Object[]> exercisesSummaryEntity = exerciseService.getExercisesSummariesAvailableForUser(userToken.getId(), pageable);
            List<ExercisesSummaryDto> exercisesSummaryDto = exercisesSummaryEntity.stream()
                    .map(result -> ExercisesSummaryDto.builder()
                            .id((Long) result[0])
                            .name((String) result[1])
                            .rate((Float) result[2])
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
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("ExercisesController::getExercisesSummaryAvailableForUser ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/exercises/{exerciseId}")
    public ResponseEntity<ExercisesDto> getExercisesByUserAndId(@RequestHeader("Authorization") String authorizationHeader,
                                                @PathVariable Long exerciseId) {
        log.info("ExercisesController::getExercisesByUserAndId START, exerciseId = {}", exerciseId);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::getExercisesByUserAndId ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<ExercisesEntity> exercise = exerciseService.getExerciseByUserAndId(userToken.getId(), exerciseId);
        if (exercise.isPresent()) {
            ExercisesEntity exercisesEntity = exercise.get();
            log.info("ExercisesController::getExerciseByUserAndId STOP exercise = {}",exercisesEntity);
            return new ResponseEntity<>(exercisesMapper.mapTo(exercisesEntity), HttpStatus.OK);
        } else {
            log.info("ExercisesController::getExerciseByUserAndId ERROR Exercise not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("exercises/exercise")
    public ResponseEntity<ExercisesDto> createExercise(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody CreateExerciseWrapper wrapper) {
        log.info("ExercisesController::createExercise START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::createExercise ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        ExercisesDto exercise = wrapper.getExercise();
        List<String> sharedWith = wrapper.getSharedWith();

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("ExercisesController::createExercise ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            log.info("ExercisesController::addExercise exercise = {}, user = {}, sharedWith = {}", exercise.toString(), user, sharedWith);
            ExercisesEntity savedExercise = exerciseService.createExercise(exercisesMapper.mapFrom(exercise), user, sharedWith);
            log.info("ExercisesController::createExercise STOP exercise = {}", savedExercise);
            return new ResponseEntity<>(exercisesMapper.mapTo(savedExercise), HttpStatus.OK);
        } catch (Exception e) {
            log.info("ExercisesController::createExercise ERROR error = {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("exercises/exercise/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(@RequestHeader("Authorization") String authorizationHeader,
                                               @PathVariable Long exerciseId) {
        log.info("ExercisesController::deleteExercise START exerciseId = {}", exerciseId);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::deleteExercise ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("ExercisesController::createSuperset ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            exerciseService.deleteExercise(exerciseId, user.getId());

            log.info("ExercisesController::deleteExercise STOP");
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            log.error("ExercisesController::deleteExercise ERROR error = {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("exercises/exercise")
    public ResponseEntity<ExercisesDto> updateExercise(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody ExercisesDto exercisesDto) {
        log.info("ExercisesController::updateExercise START, exerciseDto = {}", exercisesDto);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::updateExercise ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        ExercisesEntity exercisesEntity = exerciseService.updateExercise(exercisesMapper.mapFrom(exercisesDto));
        log.info("ExercisesController::updateExercise STOP, exerciseEntity = {}", exercisesEntity.toString());

        return new ResponseEntity<>(exercisesMapper.mapTo(exercisesEntity), HttpStatus.OK);
    }

    @GetMapping("exercises/rate/{exerciseId}/{rate}")
    public ResponseEntity<Float> rateExercise(@RequestHeader("Authorization") String authorizationHeader,
                                                     @PathVariable Long exerciseId,
                                                     @PathVariable Integer rate) {
        log.info("ExercisesController::rateExercise START, exerciseId = {}, rate = {}", exerciseId, rate);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::rateExercise ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Float exerciseNewRate = exerciseService.rateExercise(exerciseId, rate);
        log.info("ExercisesController::rateExercise STOP");
        return new ResponseEntity<>(exerciseNewRate, HttpStatus.OK);
    }

    @GetMapping("/supersets/summaries")
    public ResponseEntity<PageDto<SupersetsSummaryDto>> getSupersetsSummaryAvailableForUser(@RequestHeader("Authorization") String authorizationHeader,
                                                                                            @RequestParam(defaultValue = "0") int page,
                                                                                            @RequestParam(defaultValue = "10") int size) {
        log.info("ExercisesController::getSupersetsSummaryAvailableForUser START");
        log.debug("ExercisesController::getSupersetsSummaryAvailableForUser page = {}, size = {}", page, size);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::getSupersetsSummaryAvailableForUser ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Object[]> supersetsSummaryEntity = exerciseService.getSupersetsSummariesAvailableForUser(userToken.getId(), pageable);
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
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("ExercisesController::getSupersetsSummaryAvailableForUser ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/supersets/{supersetId}")
    public ResponseEntity<SupersetsDto> getSupersetByUserAndId(@RequestHeader("Authorization") String authorizationHeader,
                                                                @PathVariable Long supersetId) {
        log.info("ExercisesController::getSupersetByUserAndId START, exerciseId = {}", supersetId);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::getSupersetByUserAndId ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<SupersetsEntity> superset = exerciseService.getSupersetByUserAndId(userToken.getId(), supersetId);
        if (superset.isPresent()) {
            SupersetsEntity supersetEntity = superset.get();
            log.info("ExercisesController::getSupersetByUserAndId STOP exercise = {}", supersetEntity);
            SupersetsDto output = SupersetsDto.builder()
                    .id(supersetEntity.getId())
                    .name(supersetEntity.getName())
                    .exercise1(supersetEntity.getExercise1().getId())
                    .exercise2(supersetEntity.getExercise2().getId())
                    .rate(supersetEntity.getRate())
                    .userCounter(supersetEntity.getUserCounter())
                    .build();
            return new ResponseEntity<>(output, HttpStatus.OK);
        } else {
            log.info("ExercisesController::getSupersetByUserAndId ERROR Exercise not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("supersets/superset")
    public ResponseEntity<SupersetsDto> createSuperset(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody CreateSupersetWrapper wrapper) {
        log.info("ExercisesController::createSuperset START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::createSuperset ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        SupersetsDto superset = wrapper.getSuperset();
        List<String> sharedWith = wrapper.getSharedWith();
        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("ExercisesController::createSuperset ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            SupersetsEntity supersetEntity = supersetsMapper.mapFrom(superset);
            supersetEntity.setCreatedBy(user);
            supersetEntity.setExercise1(exerciseService.getExerciseByUserAndId(userToken.getId(), superset.getExercise1()).get());
            supersetEntity.setExercise2(exerciseService.getExerciseByUserAndId(userToken.getId(), superset.getExercise2()).get());
            log.info("ExercisesController::addSuperset superset = {}, user = {}, sharedWith = {}", superset.toString(), user, sharedWith);
            SupersetsEntity savedSuperset = exerciseService.createSuperset(supersetEntity, user, sharedWith);
            log.info("ExercisesController::createSuperset STOP superset = {}", savedSuperset);
            SupersetsDto output = SupersetsDto.builder()
                    .id(savedSuperset.getId())
                    .name(savedSuperset.getName())
                    .exercise1(savedSuperset.getExercise1().getId())
                    .exercise2(savedSuperset.getExercise2().getId())
                    .rate(savedSuperset.getRate())
                    .userCounter(savedSuperset.getUserCounter())
                    .build();
            return new ResponseEntity<>(output, HttpStatus.OK);
        } catch (Exception e) {
            log.info("ExercisesController::createSuperset ERROR error = {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("supersets/superset/{supersetId}")
    public ResponseEntity<Void> deleteSuperset(@RequestHeader("Authorization") String authorizationHeader,
                                               @PathVariable Long supersetId) {
        log.info("ExercisesController::deleteSuperset START supersetId = {}", supersetId);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::deleteSuperset ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("ExercisesController::createSuperset ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            exerciseService.deleteSuperset(supersetId, user.getId());
        } catch (Exception e) {
            log.error("ExercisesController::deleteSuperset ERROR error = {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("ExercisesController::deleteSuperset STOP");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("supersets/superset")
    public ResponseEntity<SupersetsDto> updateSuperset(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody SupersetsDto supersetsDto) {
        log.info("ExercisesController::updateSuperset START, supersetDto = {}", supersetsDto);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::updateSuperset ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SupersetsEntity supersetsEntity = supersetsMapper.mapFrom(supersetsDto);
        supersetsEntity.setCreatedBy(userRepository.findById(userToken.getId()).orElse(null));
        SupersetsEntity savedSuperset = exerciseService.updateSuperset(supersetsEntity);

        SupersetsDto output = SupersetsDto.builder()
                .id(savedSuperset.getId())
                .name(savedSuperset.getName())
                .exercise1(savedSuperset.getExercise1().getId())
                .exercise2(savedSuperset.getExercise2().getId())
                .rate(savedSuperset.getRate())
                .userCounter(savedSuperset.getUserCounter())
                .build();
        log.info("ExercisesController::updateSuperset STOP, exerciseEntity = {}", output.toString());

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("supersets/rate/{supersetId}/{rate}")
    public ResponseEntity<Float> rateSuperset(@RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable Long supersetId,
                                             @PathVariable Integer rate) {
        log.info("ExercisesController::rateSuperset START, supersetId = {}, rate = {}", supersetId, rate);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::rateSuperset ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Float supersetNewRate = exerciseService.rateSuperset(supersetId, rate);
        log.info("ExercisesController::rateSuperset STOP");
        return new ResponseEntity<>(supersetNewRate, HttpStatus.OK);
    }

}
