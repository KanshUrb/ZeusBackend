package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.trainings.TrainingsHistoryDto;
import com.kansh.zeus.domain.entities.trainings.TrainingsHistoryEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainingHistoryMapperImpl implements Mapper<TrainingsHistoryEntity, TrainingsHistoryDto> {

    private final ModelMapper modelMapper;

    @Override
    public TrainingsHistoryDto mapTo(TrainingsHistoryEntity entity) { return modelMapper.map(entity, TrainingsHistoryDto.class); }

    @Override
    public TrainingsHistoryEntity mapFrom(TrainingsHistoryDto dto) { return modelMapper.map(dto, TrainingsHistoryEntity.class); }

}
