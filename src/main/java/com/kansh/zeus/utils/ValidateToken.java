package com.kansh.zeus.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class ValidateToken {

    public UserTokenDto validateToken(String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        log.info("validateToken: JWT Token: {}", token);

        FirebaseToken decodedToken;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        } catch (FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error! " + e);
        }

        if (decodedToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token!");
        }

        String[] names = new String[]{"",""};
        if(decodedToken.getName() != null) {
            names = decodedToken.getName().split(" ");
        }

        return UserTokenDto.builder()
                .id(decodedToken.getUid())
                .email(decodedToken.getEmail())
                .firstName(names[0])
                .lastName(names[1])
                .photo(decodedToken.getPicture())
                .build();
    }
}
