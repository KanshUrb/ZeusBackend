package com.kansh.zeus.domain.dto.friends;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private Long id;

    private String content;

    private Timestamp createdAt;

    private Integer likesCount;

    private Set<PostLikeDto> likes;

    private Set<PostCommentDto> comments;
}
