package com.kansh.zeus.controllers;

import com.kansh.zeus.domain.dto.users.UsersDto;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.config.ValidateToken;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final ValidateToken validateToken;

    private final UserService userService;

    private final Mapper<UsersEntity, UsersDto> userMapper;

    @Autowired
    public UserController(Mapper<UsersEntity, UsersDto> userMapper, ValidateToken validateToken, UserService userService) {
        this.userMapper = userMapper;
        this.validateToken = validateToken;
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UsersDto> login(@RequestHeader("Authorization") String authorizationHeader,
                                                      @RequestBody Map<String, Object> requestBody) {
        log.info("UserController:login START");
        log.info(requestBody.toString());

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("UserController:login ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (userService.checkIfUserExists(userToken.getId())) {
            log.info("UserController:login: try to log in");

            try {
                UsersEntity userEntity = userService.loginUser(userToken.getId());
                UsersDto userDto = userMapper.mapTo(userEntity);
                log.info("UserController:login: STOP logging successful userDto: id = {}, firstName = {}, lastName = {}, photo = {}", userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getPhoto().substring(0, 10));
                return new ResponseEntity<>(userDto, HttpStatus.OK);
            } catch (Exception e) {
                log.info("UserController:login: ERROR while logging {}", e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            log.info("UserController:login: try to register");

            try {
                log.info(requestBody.toString());
                UsersEntity userEntity = userService.registerUser(userToken,
                        requestBody.get("firstName").toString(),
                        requestBody.get("lastName").toString(),
                        Integer.parseInt(requestBody.get("gender").toString()),
                        requestBody.get("photo").toString());

                UsersDto userDto = userMapper.mapTo(userEntity);
                log.info("UserController:login: STOP register successful userDto: id = {}, firstName = {}, lastName = {}, photo = {}", userDto.getId(), userDto.getFirstName(), userDto.getLastName(), userDto.getPhoto().substring(0, 10));
                return new ResponseEntity<>(userDto, HttpStatus.OK);

            } catch(Exception e) {
                log.info("UserController:login: ERROR while registering {}", e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    @GetMapping(value = "/login")
    public ResponseEntity<Boolean> checkIfUserExists(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("UserController:getUserData START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("UserController:getUsere ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            boolean user = userService.checkIfUserExists(userToken.getId());
            log.info("UserController:getUserData STOP user exist: {}", user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e) {
            log.info("UserController:getUserData: ERROR while logging {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
