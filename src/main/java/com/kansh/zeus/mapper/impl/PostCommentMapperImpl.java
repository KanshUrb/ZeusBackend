package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.friends.PostCommentDto;
import com.kansh.zeus.domain.entities.friends.PostCommentEntity;
import com.kansh.zeus.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostCommentMapperImpl implements Mapper<PostCommentEntity, PostCommentDto> {

    private final ModelMapper modelMapper;

    @Override
    public PostCommentDto mapTo(PostCommentEntity entity) { return this.modelMapper.map(entity, PostCommentDto.class); }

    @Override
    public PostCommentEntity mapFrom(PostCommentDto dto) { return this.modelMapper.map(dto, PostCommentEntity.class); }

}
