package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.friends.PostDto;
import com.kansh.zeus.domain.entities.friends.PostEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostMapperImpl implements Mapper<PostEntity, PostDto> {

    private final ModelMapper modelMapper;

    @Override
    public PostDto mapTo(PostEntity entity) { return this.modelMapper.map(entity, PostDto.class); }

    @Override
    public PostEntity mapFrom(PostDto dto) { return this.modelMapper.map(dto, PostEntity.class); }

}
