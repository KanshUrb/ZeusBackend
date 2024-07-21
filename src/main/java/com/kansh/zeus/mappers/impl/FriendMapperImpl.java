package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.friends.FriendDto;
import com.kansh.zeus.domain.entities.friends.FriendEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FriendMapperImpl implements Mapper<FriendEntity, FriendDto> {

    private final ModelMapper modelMapper;

    public FriendMapperImpl(ModelMapper modelMapper) { this.modelMapper = modelMapper; }

    @Override
    public FriendDto mapTo(FriendEntity friendEntity) { return this.modelMapper.map(friendEntity, FriendDto.class); }

    @Override
    public FriendEntity mapFrom(FriendDto friendDto) { return this.modelMapper.map(friendDto, FriendEntity.class); }

}
