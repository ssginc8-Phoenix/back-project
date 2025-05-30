package com.ssginc8.docto.patients.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssginc8.docto.patient.dto.PatientRequest;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.repo.PatientRepo;
import com.ssginc8.docto.restdocs.RestDocsConfig;
import com.ssginc8.docto.user.entity.LoginType;
import com.ssginc8.docto.user.entity.Role;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.repo.UserRepo;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("prod")
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@Import(RestDocsConfig.class)
public class PatientControllerTest {

	@Autowired protected RestDocumentationResultHandler restDocs;
	private MockMvc mockMvc;

	@Autowired private WebApplicationContext context;
	@Autowired private ObjectMapper objectMapper;

	@Autowired private EntityManager em;
	@Autowired private PatientRepo patientRepo;
	@Autowired private UserRepo userRepo;

	private Long savedPatientId;
	private Long testUserId; // 등록 테스트용

	@BeforeEach
	void setUp(RestDocumentationContextProvider provider) {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(provider))
			.alwaysDo(restDocs)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();

		// 테스트용 유저 생성 (UUID와 이메일은 시간 기반으로 유일하게 보장)
		String uniqueId = String.valueOf(System.currentTimeMillis());
		User user = User.createUser(
			"uuid-" + uniqueId,
			"user" + uniqueId + "@example.com",
			"1234",
			"테스트유저",
			"01099998888",
			false,
		"asd123212222222"

		);
		User savedUser = userRepo.save(user);
		testUserId = savedUser.getUserId();

		// 해당 유저로 환자 미리 하나 생성 (삭제 테스트용)
		Patient patient = patientRepo.save(Patient.create(savedUser, "900101-1234567"));
		savedPatientId = patient.getPatientId();
	}

	@Test
	@DisplayName("환자 등록")
	void createPatient() throws Exception {
		// 테스트용 다른 유저 생성
		String uniqueId = String.valueOf(System.currentTimeMillis() + 1); // 밀리초 차이로 충돌 방지
		User user = User.createUser(
			"uuid-" + uniqueId,
			"user" + uniqueId + "@example.com",
			"1234",
			"테스트유저",
			"01099998888",
			false,
			"asd123212222222"

		);
		User savedNewUser = userRepo.save(user);

		PatientRequest request = new PatientRequest(savedNewUser.getUserId(), "010101-2345678");

		mockMvc.perform(post("/api/v1/patients")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("userId").description("User ID"),
					fieldWithPath("residentRegistrationNumber").description("주민등록번호")
				),
				responseFields(
					fieldWithPath("patientId").description("환자 ID"),
					fieldWithPath("userId").description("유저 ID"),
					fieldWithPath("residentRegistrationNumber").description("주민등록번호")
				)
			));
	}

	@Test
	@DisplayName("환자 전체 조회")
	void getAllPatients() throws Exception {
		mockMvc.perform(get("/api/v1/patients"))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("[].patientId").description("환자 ID"),
					fieldWithPath("[].userId").description("유저 ID"),
					fieldWithPath("[].residentRegistrationNumber").description("주민등록번호")
				)
			));
	}

	@Test
	@DisplayName("환자 삭제 (소프트 삭제)")
	void deletePatient() throws Exception {
		mockMvc.perform(delete("/api/v1/patients/{patientId}", savedPatientId))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("patientId").description("삭제할 환자 ID")
				)
			));
	}
}
