package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.trainings.TrainingsItemsDto;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainingItemMapperImpl implements Mapper<TrainingsItemsEntity, TrainingsItemsDto> {

    private final ModelMapper modelMapper;

    @Override
    public TrainingsItemsDto mapTo(TrainingsItemsEntity entity) { return modelMapper.map(entity, TrainingsItemsDto.class); }

    @Override
    public TrainingsItemsEntity mapFrom(TrainingsItemsDto dto) { return modelMapper.map(dto, TrainingsItemsEntity.class); }

}
