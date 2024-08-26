package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.friends.FriendDto;
import com.kansh.zeus.domain.entities.friends.FriendEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FriendMapperImpl implements Mapper<FriendEntity, FriendDto> {

    private final ModelMapper modelMapper;

    @Override
    public FriendDto mapTo(FriendEntity friendEntity) { return this.modelMapper.map(friendEntity, FriendDto.class); }

    @Override
    public FriendEntity mapFrom(FriendDto friendDto) { return this.modelMapper.map(friendDto, FriendEntity.class); }

}
