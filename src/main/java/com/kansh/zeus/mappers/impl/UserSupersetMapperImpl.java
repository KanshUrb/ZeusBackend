package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.exercises.UserSupersetsDto;
import com.kansh.zeus.domain.entities.exercises.UserSupersetsEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserSupersetMapperImpl implements Mapper<UserSupersetsEntity, UserSupersetsDto> {

    private final ModelMapper modelMapper;

    public UserSupersetMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public UserSupersetsDto mapTo(UserSupersetsEntity entity) { return modelMapper.map(entity, UserSupersetsDto.class); }

    @Override
    public UserSupersetsEntity mapFrom(UserSupersetsDto dto) { return modelMapper.map(dto, UserSupersetsEntity.class); }

}
