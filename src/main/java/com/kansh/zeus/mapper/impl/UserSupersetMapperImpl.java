package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.exercises.UserSupersetsDto;
import com.kansh.zeus.domain.entities.exercises.UserSupersetsEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserSupersetMapperImpl implements Mapper<UserSupersetsEntity, UserSupersetsDto> {

    private final ModelMapper modelMapper;

    @Override
    public UserSupersetsDto mapTo(UserSupersetsEntity entity) { return modelMapper.map(entity, UserSupersetsDto.class); }

    @Override
    public UserSupersetsEntity mapFrom(UserSupersetsDto dto) { return modelMapper.map(dto, UserSupersetsEntity.class); }

}
