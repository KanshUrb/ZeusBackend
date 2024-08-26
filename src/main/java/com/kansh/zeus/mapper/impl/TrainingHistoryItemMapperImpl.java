package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.trainings.TrainingsHistoryItemsDto;
import com.kansh.zeus.domain.entities.trainings.TrainingsHistoryItemsEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainingHistoryItemMapperImpl implements Mapper<TrainingsHistoryItemsEntity, TrainingsHistoryItemsDto> {

    private final ModelMapper modelMapper;

    @Override
    public TrainingsHistoryItemsDto mapTo(TrainingsHistoryItemsEntity entity) { return modelMapper.map(entity, TrainingsHistoryItemsDto.class); }

    @Override
    public TrainingsHistoryItemsEntity mapFrom(TrainingsHistoryItemsDto dto) { return modelMapper.map(dto, TrainingsHistoryItemsEntity.class); }

}
