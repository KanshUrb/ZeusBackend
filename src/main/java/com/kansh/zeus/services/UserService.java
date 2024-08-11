package com.kansh.zeus.services;

import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;

public interface UserService {

    boolean checkIfUserExists(String userId);

    UsersEntity loginUser(String userId);

    UsersEntity registerUser(UserTokenDto userToken, String firstName, String lastName, Integer gender, String photo);

}
