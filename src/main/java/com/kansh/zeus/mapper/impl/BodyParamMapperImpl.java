package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.body_params.BodyParamsDto;
import com.kansh.zeus.domain.entities.body_params.BodyParamsEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BodyParamMapperImpl implements Mapper<BodyParamsEntity, BodyParamsDto> {

    private final ModelMapper modelMapper;

    @Override
    public BodyParamsDto mapTo(BodyParamsEntity entity) { return modelMapper.map(entity, BodyParamsDto.class); }

    @Override
    public BodyParamsEntity mapFrom(BodyParamsDto dto) { return modelMapper.map(dto, BodyParamsEntity.class); }

}
