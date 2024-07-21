package com.kansh.zeus.domain.dto.friends;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendDto {

    private String userId;

    private String friendId;

}
