package com.ssginc8.docto.init;

import com.ssginc8.docto.user.entity.Role;
import com.ssginc8.docto.user.service.UserService;
import com.ssginc8.docto.user.service.dto.AddUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DummyDataInitializer implements CommandLineRunner {

	private final UserService userService;

	@Override
	public void run(String... args) throws Exception {
		createDummyUsers();
	}

	private void createDummyUsers() {
		log.info("🎯 Dummy 사용자 데이터를 생성합니다...");

		createUser("patient@example.com", "환자 홍길동", Role.PATIENT);
		createUser("guardian@example.com", "보호자 김철수", Role.GUARDIAN);
		createUser("admin@example.com", "병원관리자 이영희", Role.HOSPITAL_ADMIN);
		createUser("doctor@example.com", "의사 박의사", Role.DOCTOR);
		createUser("sysadmin@example.com", "시스템관리자 관리자", Role.SYSTEM_ADMIN);

		log.info("✅ 더미 사용자 생성 완료");
	}

	private void createUser(String email, String name, Role role) {
		AddUser.Request request = AddUser.Request.builder()
			.email(email)
			.password("Test13579!") // 암호화는 내부에서 처리됨
			.name(name)
			.phone("010-0000-" + (int) (Math.random() * 9000 + 1000))
			.address("서울시 강남구")
			.role(Role.valueOf(role.name()))
			.build();

		try {
			userService.createUser(request);
		} catch (Exception e) {
			log.warn("⚠️ 사용자 생성 실패 - {}: {}", email, e.getMessage());
		}
	}
}

