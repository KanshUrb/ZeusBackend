package com.kansh.zeus.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Bean
    public boolean initialization() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream("./src/main/resources/serviceAccountKey.json");

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                log.info("Firebase initialized successfully.");
            } else {
                log.info("FirebaseApp already initialized.");
            }
            return true;
        } catch (IOException e) {
            log.error("Failed to initialize Firebase Admin SDK", e);
            return false;
        }
    }
}

