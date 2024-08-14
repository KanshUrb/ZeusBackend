package com.kansh.zeus.domain.dto.users;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserInputDto {

    private String firstName;
    private String lastName;
    private Integer gender;
    private String photo;

}
