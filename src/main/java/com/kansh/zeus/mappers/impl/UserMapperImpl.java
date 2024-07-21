package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.users.UsersDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<UsersEntity, UsersDto> {

    private final ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UsersDto mapTo(UsersEntity entity) {
        return modelMapper.map(entity, UsersDto.class);
    }

    @Override
    public UsersEntity mapFrom(UsersDto dto) {
        return modelMapper.map(dto, UsersEntity.class);
    }
}
