package com.kansh.zeus.service;

import com.kansh.zeus.domain.dto.users.UserInputDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;

public interface UserService {

    boolean checkIfUserExists(String userId);

    UsersEntity registerUser(UserTokenDto userToken, UserInputDto userInputDto);

    UsersEntity loginUser(String userId);

    UsersEntity login(UserTokenDto userToken, UserInputDto userInputDto);

}
