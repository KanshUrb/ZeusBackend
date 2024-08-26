package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.trainings.TrainingsItemsSeriesDto;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsSeriesEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainingItemSeriesMapperImpl implements Mapper<TrainingsItemsSeriesEntity, TrainingsItemsSeriesDto> {

    private final ModelMapper modelMapper;

    @Override
    public TrainingsItemsSeriesDto mapTo(TrainingsItemsSeriesEntity entity) { return modelMapper.map(entity, TrainingsItemsSeriesDto.class); }

    @Override
    public TrainingsItemsSeriesEntity mapFrom(TrainingsItemsSeriesDto dto) { return modelMapper.map(dto, TrainingsItemsSeriesEntity.class); }

}
