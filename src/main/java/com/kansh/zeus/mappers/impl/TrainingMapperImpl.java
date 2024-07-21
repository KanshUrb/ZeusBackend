package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.trainings.TrainingsDto;
import com.kansh.zeus.domain.entities.trainings.TrainingsEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapperImpl implements Mapper<TrainingsEntity, TrainingsDto> {

    private final ModelMapper modelMapper;

    public TrainingMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TrainingsDto mapTo(TrainingsEntity entity) { return modelMapper.map(entity, TrainingsDto.class); }

    @Override
    public TrainingsEntity mapFrom(TrainingsDto dto) { return modelMapper.map(dto, TrainingsEntity.class); }

}
