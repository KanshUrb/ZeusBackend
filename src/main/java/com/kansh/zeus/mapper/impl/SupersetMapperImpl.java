package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.exercises.SupersetsDto;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SupersetMapperImpl implements Mapper<SupersetsEntity, SupersetsDto> {

    private final ModelMapper modelMapper;

    @Override
    public SupersetsDto mapTo(SupersetsEntity entity) { return modelMapper.map(entity, SupersetsDto.class); }

    @Override
    public SupersetsEntity mapFrom(SupersetsDto dto) { return modelMapper.map(dto, SupersetsEntity.class); }
}
