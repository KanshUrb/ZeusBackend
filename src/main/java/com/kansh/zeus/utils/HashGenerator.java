package com.kansh.zeus.utils;

import com.kansh.zeus.repositories.users.UserRepository;
import org.springframework.stereotype.Component;
import java.util.Random;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class HashGenerator {

    private final UserRepository userRepository;

    public HashGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateHash(String userId) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(userId.getBytes());
            Random random = new Random();

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                int randomIndex = random.nextInt(hashBytes.length);
                String hex = Integer.toHexString(0xff & hashBytes[randomIndex]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public boolean checkIfHashIsAvailable(String hash) {
        return userRepository.checkIfHashIsAvailable(hash);
    }

}
