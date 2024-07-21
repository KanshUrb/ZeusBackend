package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.exercises.UserExercisesDto;
import com.kansh.zeus.domain.entities.exercises.UserExercisesEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserExerciseMapperImpl implements Mapper<UserExercisesEntity, UserExercisesDto> {

    private final ModelMapper modelMapper;

    public UserExerciseMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserExercisesDto mapTo(UserExercisesEntity entity) { return modelMapper.map(entity, UserExercisesDto.class); }

    @Override
    public UserExercisesEntity mapFrom(UserExercisesDto dto) { return modelMapper.map(dto, UserExercisesEntity.class); }
}
