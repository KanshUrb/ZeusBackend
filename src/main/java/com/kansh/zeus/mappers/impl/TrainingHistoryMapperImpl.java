package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.trainings.TrainingsHistoryDto;
import com.kansh.zeus.domain.entities.trainings.TrainingsHistoryEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TrainingHistoryMapperImpl implements Mapper<TrainingsHistoryEntity, TrainingsHistoryDto> {

    private final ModelMapper modelMapper;

    public TrainingHistoryMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TrainingsHistoryDto mapTo(TrainingsHistoryEntity entity) { return modelMapper.map(entity, TrainingsHistoryDto.class); }

    @Override
    public TrainingsHistoryEntity mapFrom(TrainingsHistoryDto dto) { return modelMapper.map(dto, TrainingsHistoryEntity.class); }

}
