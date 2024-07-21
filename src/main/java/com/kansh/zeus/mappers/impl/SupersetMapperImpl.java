package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.exercises.SupersetsDto;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SupersetMapperImpl implements Mapper<SupersetsEntity, SupersetsDto> {

    private final ModelMapper modelMapper;

    public SupersetMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SupersetsDto mapTo(SupersetsEntity entity) { return modelMapper.map(entity, SupersetsDto.class); }

    @Override
    public SupersetsEntity mapFrom(SupersetsDto dto) { return modelMapper.map(dto, SupersetsEntity.class); }
}
