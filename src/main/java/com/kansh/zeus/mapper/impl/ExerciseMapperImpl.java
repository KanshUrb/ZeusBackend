package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.exercises.ExercisesDto;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ExerciseMapperImpl implements Mapper<ExercisesEntity, ExercisesDto> {

    private final ModelMapper modelMapper;

    @Override
    public ExercisesDto mapTo(ExercisesEntity entity) { return this.modelMapper.map(entity, ExercisesDto.class); }

    @Override
    public ExercisesEntity mapFrom(ExercisesDto dto) { return modelMapper.map(dto, ExercisesEntity.class); }

}
