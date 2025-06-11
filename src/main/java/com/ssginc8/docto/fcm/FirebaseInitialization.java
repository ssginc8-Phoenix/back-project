package com.ssginc8.docto.fcm;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

/**
 * Firebase 초기화 설정 클래스
 * */

@Service
public class FirebaseInitialization {

	@PostConstruct
	public void initialize() {
		try {
			FileInputStream serviceAccount =
				new FileInputStream("./src/main/resources/firebase-service-account.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

			// FirebaseApp 초기화 (중복 초기화 방지)
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
