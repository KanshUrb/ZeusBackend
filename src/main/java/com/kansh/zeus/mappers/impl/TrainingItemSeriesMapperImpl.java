package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.trainings.TrainingsItemsSeriesDto;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsSeriesEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TrainingItemSeriesMapperImpl implements Mapper<TrainingsItemsSeriesEntity, TrainingsItemsSeriesDto> {

    private final ModelMapper modelMapper;

    public TrainingItemSeriesMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TrainingsItemsSeriesDto mapTo(TrainingsItemsSeriesEntity entity) { return modelMapper.map(entity, TrainingsItemsSeriesDto.class); }

    @Override
    public TrainingsItemsSeriesEntity mapFrom(TrainingsItemsSeriesDto dto) { return modelMapper.map(dto, TrainingsItemsSeriesEntity.class); }

}
