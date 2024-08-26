package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.users.UserDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapperImpl implements Mapper<UsersEntity, UserDto> {

    private final ModelMapper modelMapper;

    @Override
    public UserDto mapTo(UsersEntity entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    @Override
    public UsersEntity mapFrom(UserDto dto) {
        return modelMapper.map(dto, UsersEntity.class);
    }
}
