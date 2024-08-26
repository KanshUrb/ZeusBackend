package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.friends.PostLikeDto;
import com.kansh.zeus.domain.entities.friends.PostLikeEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostLikeMapperImpl implements Mapper<PostLikeEntity, PostLikeDto> {

    private final ModelMapper modelMapper;

    @Override
    public PostLikeDto mapTo(PostLikeEntity entity) { return this.modelMapper.map(entity, PostLikeDto.class); }

    @Override
    public PostLikeEntity mapFrom(PostLikeDto dto) { return this.modelMapper.map(dto, PostLikeEntity.class); }

}
