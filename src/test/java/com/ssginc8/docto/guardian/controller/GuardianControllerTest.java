package com.ssginc8.docto.guardian.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssginc8.docto.guardian.dto.GuardianStatusRequest;
import com.ssginc8.docto.restdocs.RestDocsConfig;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@Import(RestDocsConfig.class)
class GuardianControllerTest {

	@Autowired
	protected RestDocumentationResultHandler restDocs;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	// 새로 생성된 테스트용 데이터
	private final Long testRequestId = 2L;            // patient_guardian_id
	private final String validInviteCode = "test-invite-014";
	private final Long guardianUserId = 14L;
	private final Long patientId = 39L;

	@BeforeEach
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation))
			.alwaysDo(MockMvcResultHandlers.print())
			.alwaysDo(restDocs)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();
	}

	@Test
	@DisplayName("보호자 권한 수락/거절 API")
	void respondToGuardianRequest() throws Exception {
		GuardianStatusRequest request = GuardianStatusRequest.builder()
			.status("ACCEPTED")                // 또는 "REJECTED"
			.inviteCode(validInviteCode)        // "test-invite-014"
			.build();

		mockMvc.perform(post("/api/v1/guardians/request/{requestId}", testRequestId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("requestId").description("보호자 요청 식별자")
				),
				requestFields(
					fieldWithPath("status").type(JsonFieldType.STRING)
						.description("변경할 권한 상태(ACCEPTED/REJECTED)"),
					fieldWithPath("inviteCode").type(JsonFieldType.STRING)
						.description("초대코드 (User.uuid)")
				)
			));
	}

	@Test
	@DisplayName("보호자-환자 매핑 해제 API")
	void deleteGuardianMapping() throws Exception {
		mockMvc.perform(delete("/api/v1/guardians/{userId}/patients/{patientId}", guardianUserId, patientId))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("userId").description("보호자(유저) 식별자"),
					parameterWithName("patientId").description("환자 식별자")
				)
			));
	}

	@Test
	@DisplayName("보호자가 가진 환자 목록 조회 API")
	void getAllAcceptedPatients() throws Exception {
		mockMvc.perform(get("/api/v1/guardians/me/patients"))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("[].patientId").type(JsonFieldType.NUMBER)
						.description("환자 식별자"),
					fieldWithPath("[].name").type(JsonFieldType.STRING)
						.description("환자 이름"),
					fieldWithPath("[].residentRegistrationNumber").type(JsonFieldType.STRING)
						.description("환자 주민등록번호")
				)
			));
	}
}
