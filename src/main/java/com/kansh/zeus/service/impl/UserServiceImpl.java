package com.kansh.zeus.service.impl;

import com.kansh.zeus.domain.dto.users.UserInputDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repository.users.UserRepository;
import com.kansh.zeus.service.UserService;
import com.kansh.zeus.utils.HashGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final HashGenerator hashGenerator;

    @Override
    public boolean checkIfUserExists(String userId) {
        log.info("UserService:checkIfUserExists START, userId {}", userId);
        Optional<UsersEntity> user = userRepository.findById(userId);
        log.info("UserService:checkIfUserExists STOP, user.isPresent() = {}", user.isPresent());
        return user.isPresent();
    }

    @Override
    public UsersEntity login(UserTokenDto userToken, UserInputDto userInputDto) {
        log.info("UserService:login START, userToken = {}, userInputDto = {}", userToken.toString(), userInputDto.toString());

        if (checkIfUserExists(userToken.getId())) {
            log.info("UserService:login: try to log in");

            UsersEntity userEntity = loginUser(userToken.getId());
            log.info("UserService:login: STOP logging successful userEntity: id = {}, firstName = {}, lastName = {}",
                    userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName());
            return userEntity;
        } else {
            log.info("UserService:login: try to register");

            UsersEntity userEntity = registerUser(userToken, userInputDto);
                log.info("UserController:login: STOP register successful userEntity: id = {}, firstName = {}, lastName = {}",
                        userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName());
                return userEntity;
        }
    }

    @Override
    public UsersEntity registerUser(UserTokenDto userToken, UserInputDto userInputDto) {
        log.info("UserService:registerUser START");
        try {
            String hash;
            do {
                hash = hashGenerator.generateHash(userToken.getId());
            } while (hashGenerator.checkIfHashIsAvailable(hash));

            UsersEntity user = userRepository.save(UsersEntity.builder()
                    .id(userToken.getId())
                    .email(userToken.getEmail())
                    .firstName(userInputDto.getFirstName())
                    .lastName(userInputDto.getLastName())
                    .gender(userInputDto.getGender())
                    .hash(hash)
                    .photo(userInputDto.getPhoto())
                    .build());
            log.info("UserService:registerUser STOP");
            return user;

        } catch (Exception e) {
            throw new RuntimeException("Error while registering user", e);
        }
    }

    @Override
    public UsersEntity loginUser(String userId) {
        log.info("UserService:loginUser START, userId = {}", userId);

        UsersEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        log.info("UserService:loginUser STOP, user = {}", user.toString());
        return user;
    }

}
