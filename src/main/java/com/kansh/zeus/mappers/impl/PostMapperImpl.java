package com.kansh.zeus.mappers.impl;

import com.kansh.zeus.domain.dto.friends.PostDto;
import com.kansh.zeus.domain.entities.friends.PostEntity;
import com.kansh.zeus.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PostMapperImpl implements Mapper<PostEntity, PostDto> {

    private final ModelMapper modelMapper;

    public PostMapperImpl(ModelMapper modelMapper) { this.modelMapper = modelMapper; }

    @Override
    public PostDto mapTo(PostEntity entity) { return this.modelMapper.map(entity, PostDto.class); }

    @Override
    public PostEntity mapFrom(PostDto dto) { return this.modelMapper.map(dto, PostEntity.class); }

}
