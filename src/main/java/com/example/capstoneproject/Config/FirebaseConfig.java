package com.example.capstoneproject.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
@PostConstruct
public void initialize() throws IOException {

//    FileInputStream serviceAccount =
//            new FileInputStream("/serviceAccountKey.json");

    InputStream serviceAccount = this.getClass().getResourceAsStream("/serviceAccountKey.json");

    FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

    FirebaseApp.initializeApp(options);
}

@Bean
    FirebaseMessaging firebaseMessaging() throws IOException{
    GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource("serviceAccountKey.json").getInputStream());
    FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(googleCredentials).build();
    FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions,"my-app");
    return FirebaseMessaging.getInstance(app);
}

}
