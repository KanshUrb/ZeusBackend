package com.kansh.zeus.services.impl;

import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.UserService;
import com.kansh.zeus.utils.HashGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final HashGenerator hashGenerator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, HashGenerator hashGenerator) {
        this.userRepository = userRepository;
        this.hashGenerator = hashGenerator;
    }

    @Override
    public boolean checkIfUserExists(String userId) {

        Optional<UsersEntity> user = userRepository.findById(userId);
        return user.isPresent();
    }

    @Override
    public UsersEntity registerUser(UserTokenDto userToken, String firstName, String lastName, Integer gender) {
        log.info("POJEBIE MNIE");
        try {
            String hash;
            do {
                hash = hashGenerator.generateHash(userToken.getId());
            } while (hashGenerator.checkIfHashIsAvailable(hash));

            return userRepository.save(UsersEntity.builder()
                    .id(userToken.getId())
                    .email(userToken.getEmail())
                    .firstName(firstName)
                    .lastName(lastName)
                    .gender(gender)
                    .hash(hash)
                    .photo(userToken.getPhoto())
                    .build());

        } catch (Exception e) {
            throw new RuntimeException("Error while registering user", e);
        }
    }

    @Override
    public UsersEntity loginUser(String userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

}
