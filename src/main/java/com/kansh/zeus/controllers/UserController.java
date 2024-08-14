package com.kansh.zeus.controllers;

import com.kansh.zeus.domain.dto.users.UserDto;
import com.kansh.zeus.domain.dto.users.UserInputDto;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.config.ValidateToken;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final ValidateToken validateToken;
    private final UserService userService;
    private final Mapper<UsersEntity, UserDto> userMapper;

    @PostMapping(value = "/login")
    public ResponseEntity<UserDto> login(@RequestHeader("Authorization") String authorizationHeader,
                                         @RequestBody UserInputDto userInputDto) {
        log.info("UserController:login START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("UserController:login ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UsersEntity userEntity = userService.login(userToken, userInputDto);
            log.info("UserController:login: STOP logging successful");
            return new ResponseEntity<>(userMapper.mapTo(userEntity), HttpStatus.OK);

        } catch (Exception e) {
            log.info("UserController:login: ERROR while logging {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/login")
    public ResponseEntity<Boolean> checkIfUserExists(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("UserController:checkIfUserExists START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("UserController:checkIfUserExists ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            boolean user = userService.checkIfUserExists(userToken.getId());
            log.info("UserController:checkIfUserExists STOP user exist: {}", user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e) {
            log.info("UserController:checkIfUserExists ERROR while logging {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
