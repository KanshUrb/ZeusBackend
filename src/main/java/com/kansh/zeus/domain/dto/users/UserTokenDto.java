package com.kansh.zeus.domain.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTokenDto {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String photo;

    @Override
    public String toString() {
        return "Uid: " + id + ", Email: " + email + ", firstName: " + firstName + ", lastName: " + lastName + ", photo: " + photo;
    }
}
