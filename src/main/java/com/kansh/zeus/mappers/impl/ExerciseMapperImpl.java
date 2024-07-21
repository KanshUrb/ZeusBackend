package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.exercises.ExercisesDto;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ExerciseMapperImpl implements Mapper<ExercisesEntity, ExercisesDto> {

    private final ModelMapper modelMapper;

    public ExerciseMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ExercisesDto mapTo(ExercisesEntity entity) { return this.modelMapper.map(entity, ExercisesDto.class); }

    @Override
    public ExercisesEntity mapFrom(ExercisesDto dto) { return modelMapper.map(dto, ExercisesEntity.class); }

}
