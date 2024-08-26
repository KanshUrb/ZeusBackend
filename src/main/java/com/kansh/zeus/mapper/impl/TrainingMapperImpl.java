package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.trainings.TrainingsDto;
import com.kansh.zeus.domain.entities.trainings.TrainingsEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainingMapperImpl implements Mapper<TrainingsEntity, TrainingsDto> {

    private final ModelMapper modelMapper;

    @Override
    public TrainingsDto mapTo(TrainingsEntity entity) { return modelMapper.map(entity, TrainingsDto.class); }

    @Override
    public TrainingsEntity mapFrom(TrainingsDto dto) { return modelMapper.map(dto, TrainingsEntity.class); }

}
