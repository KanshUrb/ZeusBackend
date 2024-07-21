package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.friends.PostLikeDto;
import com.kansh.zeus.domain.entities.friends.PostLikeEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PostLikeMapperImpl implements Mapper<PostLikeEntity, PostLikeDto> {

    private final ModelMapper modelMapper;

    public PostLikeMapperImpl(ModelMapper modelMapper) { this.modelMapper = modelMapper; }

    @Override
    public PostLikeDto mapTo(PostLikeEntity entity) { return this.modelMapper.map(entity, PostLikeDto.class); }

    @Override
    public PostLikeEntity mapFrom(PostLikeDto dto) { return this.modelMapper.map(dto, PostLikeEntity.class); }

}
