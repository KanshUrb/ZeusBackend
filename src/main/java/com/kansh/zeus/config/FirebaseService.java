package com.kansh.zeus.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    public String processFirebaseToken(String firebaseToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);
            String uid = decodedToken.getUid();
            return uid;
        } catch (Exception e) {
        }
        return null;
    }
}
