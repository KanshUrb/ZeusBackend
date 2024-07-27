package com.kansh.zeus.controllers;

import com.kansh.zeus.config.ValidateToken;
import com.kansh.zeus.domain.dto.friends.FriendDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.dto.exercises.*;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.mappers.impl.UserEntityToFriendDtoMapper;
import com.kansh.zeus.repositories.exercises.UserExerciseRepository;
import com.kansh.zeus.repositories.exercises.UserSupersetRepository;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    private final UserEntityToFriendDtoMapper userEntityToFriendDtoMapper;

    private final ValidateToken validateToken;
    private final UserExerciseRepository userExerciseRepository;
    private final UserSupersetRepository userSupersetRepository;

    @Autowired
    public ExercisesController(ExerciseService exerciseService,
                               UserRepository userRepository,
                               ValidateToken validateToken,
                               Mapper<ExercisesEntity, ExercisesDto> exercisesMapper,
                               Mapper<SupersetsEntity, SupersetsDto> supersetsMapper,
                               UserEntityToFriendDtoMapper userEntityToFriendDtoMapper, UserExerciseRepository userExerciseRepository, UserSupersetRepository userSupersetRepository) {
        this.exerciseService = exerciseService;
        this.userRepository = userRepository;
        this.validateToken = validateToken;
        this.exercisesMapper = exercisesMapper;
        this.supersetsMapper = supersetsMapper;
        this.userEntityToFriendDtoMapper = userEntityToFriendDtoMapper;
        this.userExerciseRepository = userExerciseRepository;
        this.userSupersetRepository = userSupersetRepository;
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
    public ResponseEntity<ExerciseDetailsDto> getExerciseDetails(@RequestHeader("Authorization") String authorizationHeader,
                                                @PathVariable Long exerciseId) {
        log.info("ExercisesController::getExercisesByUserAndId START, exerciseId = {}", exerciseId);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::getExercisesByUserAndId ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<ExercisesEntity> exercise = exerciseService.getExerciseByUserAndId(userToken.getId(), exerciseId);
        List<FriendDto> sharedWith = exerciseService.getSharedWith(1, exerciseId).stream().map(userEntityToFriendDtoMapper::mapToFriendDto).toList();
        if (exercise.isPresent()) {
            ExerciseDetailsDto output = ExerciseDetailsDto.builder()
                    .exercise(exercisesMapper.mapTo(exercise.get()))
                    .sharedWith(sharedWith)
                    .build();

            log.info("ExercisesController::getExerciseByUserAndId STOP exerciseDetails = {}", output);
            return new ResponseEntity<>(output, HttpStatus.OK);
        } else {
            log.info("ExercisesController::getExerciseByUserAndId ERROR Exercise not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("exercises/exercise")
    public ResponseEntity<ExercisesDto> createExercise(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody ExerciseWrapperDto wrapper) {
        log.info("ExercisesController::createExercise START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::createExercise ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        ExercisesDto exercise = wrapper.getExercise();
        List<FriendDto> sharedWith = wrapper.getSharedWith();

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("ExercisesController::createExercise ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            log.info("ExercisesController::addExercise exercise = {}, user = {}, sharedWith = {}", exercise.toString(), user, sharedWith);
            ExercisesEntity exercisesEntity = exercisesMapper.mapFrom(exercise);
            ExercisesEntity savedExercise = exerciseService.createExercise(exercisesEntity, user, sharedWith.stream().map(FriendDto::getHash).toList());
            log.info("ExercisesController::createExercise STOP exercise = {}", savedExercise);
            return new ResponseEntity<>(exercisesMapper.mapTo(savedExercise), HttpStatus.OK);
        } catch (Exception e) {
            log.info("ExercisesController::createExercise ERROR error = {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
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
                log.error("ExercisesController::deleteExercise ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            userExerciseRepository.deleteByExercise_Id(exerciseId);
            exerciseService.deleteExercise(exerciseId, user.getId());

            log.info("ExercisesController::deleteExercise STOP");
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            log.error("ExercisesController::deleteExercise ERROR error = {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("exercises/exercise")
    public ResponseEntity<ExerciseWrapperDto> updateExercise(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody ExerciseWrapperDto exerciseWrapperDto) {
        log.info("ExercisesController::updateExercise START, exerciseWrapperDto = {}", exerciseWrapperDto.toString());

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::updateExercise ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        ExercisesEntity currentExercise = exerciseService.getExerciseByUserAndId(userToken.getId(), exerciseWrapperDto.getExercise().getId()).orElse(null);
        if (isNull(currentExercise)) {
            log.error("ExercisesController::updateExercise ERROR : Exercise not found!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ExerciseWrapperDto output = exerciseService.updateExercise(userToken.getId(), currentExercise, exerciseWrapperDto);

        log.info("ExercisesController::updateExercise STOP, exerciseWrapperDto = {}", output.toString());

        return new ResponseEntity<>(output, HttpStatus.OK);
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
    public ResponseEntity<SupersetOutputDto> getSupersetByUserAndId(@RequestHeader("Authorization") String authorizationHeader,
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
            SupersetOutputDto output = SupersetOutputDto.builder()
                    .id(supersetEntity.getId())
                    .name(supersetEntity.getName())
                    .exercise1(exercisesMapper.mapTo(supersetEntity.getExercise1()))
                    .exercise2(exercisesMapper.mapTo(supersetEntity.getExercise2()))
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
                                                       @RequestBody SupersetWrapperDto wrapper) {
        log.info("ExercisesController::createSuperset START, supersetWrapperDto = {}", wrapper);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::createSuperset ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        SupersetsDto superset = wrapper.getSuperset();
        List<FriendDto> sharedWith = wrapper.getSharedWith();
        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("ExercisesController::createSuperset ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            log.info("ExercisesController::addSuperset superset = {}, user = {}, sharedWith = {}", superset.toString(), user, sharedWith);
            SupersetsEntity supersetEntity = SupersetsEntity.builder()
                    .name(superset.getName())
                    .exercise1(exerciseService.getExerciseById(superset.getExercise1()).orElse(null))
                    .exercise2(exerciseService.getExerciseById(superset.getExercise2()).orElse(null))
                    .rate(superset.getRate())
                    .build();

            supersetEntity = exerciseService.createSuperset(supersetEntity, user, sharedWith.stream().map(FriendDto::getHash).toList());

            log.info("ExercisesController::createSuperset STOP superset = {}", supersetEntity);
            SupersetsDto output = SupersetsDto.builder()
                    .id(supersetEntity.getId())
                    .name(supersetEntity.getName())
                    .exercise1(supersetEntity.getExercise1().getId())
                    .exercise2(supersetEntity.getExercise2().getId())
                    .rate(supersetEntity.getRate())
                    .userCounter(supersetEntity.getUserCounter())
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
                log.error("ExercisesController::deleteSuperset ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            userSupersetRepository.deleteBySuperset_Id(supersetId);
            exerciseService.deleteSuperset(supersetId, user.getId());
        } catch (Exception e) {
            log.error("ExercisesController::deleteSuperset ERROR error = {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("ExercisesController::deleteSuperset STOP");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("supersets/superset")
    public ResponseEntity<SupersetWrapperDto> updateSuperset(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestBody SupersetWrapperDto supersetsWrapper) {
        log.info("ExercisesController::updateSuperset START, supersetDto = {}", supersetsWrapper.getSuperset());

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("ExercisesController::updateSuperset ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        SupersetsEntity currentSuperset = exerciseService.getSupersetByUserAndId(userToken.getId(), supersetsWrapper.getSuperset().getId()).orElse(null);
        if (isNull(currentSuperset)) {
            log.error("ExercisesController::updateSuperset ERROR : Superset not found!");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SupersetWrapperDto output = exerciseService.updateSuperset(userToken.getId(), currentSuperset, supersetsWrapper);

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
