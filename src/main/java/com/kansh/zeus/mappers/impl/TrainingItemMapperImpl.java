package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.trainings.TrainingsItemsDto;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TrainingItemMapperImpl implements Mapper<TrainingsItemsEntity, TrainingsItemsDto> {

    private final ModelMapper modelMapper;

    public TrainingItemMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TrainingsItemsDto mapTo(TrainingsItemsEntity entity) { return modelMapper.map(entity, TrainingsItemsDto.class); }

    @Override
    public TrainingsItemsEntity mapFrom(TrainingsItemsDto dto) { return modelMapper.map(dto, TrainingsItemsEntity.class); }

}
