package com.kansh.zeus.utils;

import com.kansh.zeus.repository.users.UserRepository;
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

            StringBuilder letterHash = new StringBuilder();
            while (letterHash.length() < 6) {
                int randomIndex = random.nextInt(hashBytes.length);
                char letter = (char) ('a' + (Math.abs(hashBytes[randomIndex]) % 26));
                letterHash.append(letter);
            }

            return letterHash.toString();

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public boolean checkIfHashIsAvailable(String hash) {
        return userRepository.checkIfHashIsAvailable(hash);
    }
}
