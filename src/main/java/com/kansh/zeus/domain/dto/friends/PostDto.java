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

    private String createdByFirstName;

    private String createdByLastName;

    private String createdByPhoto;

    private String createdById;

    private Integer likesCount;

    private Boolean isLiked;

}
