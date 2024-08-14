package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.users.UserDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<UsersEntity, UserDto> {

    private final ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto mapTo(UsersEntity entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    @Override
    public UsersEntity mapFrom(UserDto dto) {
        return modelMapper.map(dto, UsersEntity.class);
    }
}
