package com.kansh.zeus.domain.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDto {

    private String id;

    private String firstName;

    private String lastName;

    private String hash;

    private String photo;
}
