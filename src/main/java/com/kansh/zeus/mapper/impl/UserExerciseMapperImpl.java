package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.exercises.UserExercisesDto;
import com.kansh.zeus.domain.entities.exercises.UserExercisesEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserExerciseMapperImpl implements Mapper<UserExercisesEntity, UserExercisesDto> {

    private final ModelMapper modelMapper;

    @Override
    public UserExercisesDto mapTo(UserExercisesEntity entity) { return modelMapper.map(entity, UserExercisesDto.class); }

    @Override
    public UserExercisesEntity mapFrom(UserExercisesDto dto) { return modelMapper.map(dto, UserExercisesEntity.class); }
}
