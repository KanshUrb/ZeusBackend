package com.kansh.zeus.mapper.impl;

import com.kansh.zeus.domain.dto.friends.FriendDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToFriendDtoMapper {

    public FriendDto mapToFriendDto(UsersEntity user) {
        return FriendDto.builder()
                .hash(user.getHash())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
