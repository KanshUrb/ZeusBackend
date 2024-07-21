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
    public ResponseEntity<UsersDto> checkIfUserExists(@RequestHeader("Authorization") String authorizationHeader,
                                                      @RequestBody Map<String, Object> requestBody) {
        log.info("UserController:checkIfUserExist START");
        log.info(requestBody.toString());

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if(validateToken.validateToken(authorizationHeader) == null) {
            log.error("UserController:checkIfUserExists ERROR : Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (userService.checkIfUserExists(userToken.getId())) {
            log.info("UserController:checkIfUserExists: try to log in");

            try {
                UsersEntity userEntity = userService.loginUser(userToken.getId());
                if(userEntity.getPhoto() == null) userEntity.setPhoto("https://fastly.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U");
                UsersDto userDto = userMapper.mapTo(userEntity);
                log.info("UserController:checkIfUserExists: STOP logging successful userDto: {}", userDto);
                return new ResponseEntity<>(userDto, HttpStatus.OK);
            } catch (Exception e) {
                log.info("UserController:checkIfUserExists: ERROR while logging {}", e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            log.info("UserController:checkIfUserExists: try to register");

            try {
                log.info(requestBody.toString());
                UsersEntity userEntity = userService.registerUser(userToken,
                        requestBody.get("firstName").toString(),
                        requestBody.get("lastName").toString(),
                        Integer.parseInt(requestBody.get("gender").toString()));

                if(userEntity.getPhoto() == null) userEntity.setPhoto("https://fastly.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U");
                UsersDto userDto = userMapper.mapTo(userEntity);
                log.info("UserController:checkIfUserExists: STOP register successful userDto: {}", userDto);
                return new ResponseEntity<>(userDto, HttpStatus.OK);

            } catch(Exception e) {
                log.info("UserController:checkIfUserExists: ERROR while registering {}", e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
