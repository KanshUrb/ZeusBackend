package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.exercises.ExercisesDto;
import com.kansh.zeus.domain.dto.exercises.SupersetOutputDto;
import com.kansh.zeus.domain.dto.exercises.SupersetsDto;
import com.kansh.zeus.domain.dto.trainings.TrainingItemDto;
import com.kansh.zeus.domain.dto.trainings.TrainingItemInputDto;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.services.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TrainingItemDtoConverter {
    public List<TrainingItemDto> convert(List<TrainingItemInputDto> inputList, String userId) {
        return inputList.stream().map(inputDto -> convertItem(inputDto, userId)).collect(Collectors.toList());
    }

    private final ExerciseService exerciseService;
    private final Mapper<ExercisesEntity, ExercisesDto> exerciseMapper;
    private final Mapper<SupersetsEntity, SupersetsDto> supersetMapper;

    @Autowired
    public TrainingItemDtoConverter(ExerciseService exerciseService,
                                    Mapper<ExercisesEntity, ExercisesDto> exerciseMapper,
                                    Mapper<SupersetsEntity, SupersetsDto> supersetMapper) {
        this.exerciseService = exerciseService;
        this.exerciseMapper = exerciseMapper;
        this.supersetMapper = supersetMapper;
    }

    private TrainingItemDto convertItem(TrainingItemInputDto inputDto, String userId) {
        ExercisesDto exercise = null;
        SupersetOutputDto superset = null;
        if (inputDto.getItemType() != null && inputDto.getItemId() != null) {
            if (inputDto.getItemType() == 1) {
                Optional<ExercisesEntity> exerciseOptional = exerciseService.getExerciseByUserAndId(userId, inputDto.getItemId());
                if (exerciseOptional.isPresent()) {
                    exercise = exerciseMapper.mapTo(exerciseOptional.get());
                } else {
                    throw new IllegalArgumentException("Exercise not found");
                }
            } else if (inputDto.getItemType() == 2) {
                Optional<SupersetsEntity> supersetOptional = exerciseService.getSupersetByUserAndId(userId, inputDto.getItemId());
                if (supersetOptional.isPresent()) {
                    superset = SupersetOutputDto.builder()
                            .id(supersetOptional.get().getId())
                            .name(supersetOptional.get().getName())
                            .exercise1(exerciseMapper.mapTo(supersetOptional.get().getExercise1()))
                            .exercise2(exerciseMapper.mapTo(supersetOptional.get().getExercise2()))
                            .rate(supersetOptional.get().getRate())
                            .build();
                } else {
                    throw new IllegalArgumentException("Superset not found");
                }
            }
        }

        return TrainingItemDto.builder()
                .itemType(inputDto.getItemType())
                .exercise(exercise)
                .superset(superset)
                .series(inputDto.getSeries())
                .build();
    }
}
