package com.kansh.zeus.domain.dto.friends;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCommentDto {

    private Long id;

    private Long postId;

    private String userId;

    private String comment;

    private Timestamp createdAt;

}
