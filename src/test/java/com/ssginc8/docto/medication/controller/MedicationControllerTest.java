package com.ssginc8.docto.medication.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssginc8.docto.medication.dto.*;
import com.ssginc8.docto.medication.entity.*;
import com.ssginc8.docto.medication.repo.*;
import com.ssginc8.docto.restdocs.RestDocsConfig;

@ActiveProfiles("prod")
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@Import(RestDocsConfig.class)
public class MedicationControllerTest {

	@Autowired protected RestDocumentationResultHandler restDocs;

	private MockMvc mockMvc;

	@Autowired private WebApplicationContext context;
	@Autowired private ObjectMapper objectMapper;

	@Autowired private MedicationInformationRepo medicationInformationRepo;
	@Autowired private MedicationAlertTimeRepo medicationAlertTimeRepo;
	@Autowired private MedicationAlertDayRepo medicationAlertDayRepo;
	@Autowired private MedicationLogRepo medicationLogRepo;

	private Long savedMedicationId;

	@BeforeEach
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation))
			.alwaysDo(restDocs)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();

		// 1. 기존 데이터 제거 (외래키 제약 순서로)
		medicationLogRepo.deleteAll();
		medicationAlertDayRepo.deleteAll();
		medicationAlertTimeRepo.deleteAll();
		medicationInformationRepo.deleteAll();

		// 2. 테스트용 데이터 삽입
		MedicationInformation info = medicationInformationRepo.save(
			MedicationInformation.create(1L, "타이레놀")
		);
		savedMedicationId = info.getMedicationId();

		MedicationAlertTime alertTime = medicationAlertTimeRepo.save(
			MedicationAlertTime.create(info, LocalDateTime.now().plusHours(1))
		);

		medicationAlertDayRepo.saveAll(List.of(
			MedicationAlertDay.create(alertTime, DayOfWeek.MONDAY),
			MedicationAlertDay.create(alertTime, DayOfWeek.TUESDAY)
		));

		// 3. 복약 로그 삽입 (복약 로그 조회 테스트용)
		medicationLogRepo.save(
			MedicationLog.create(alertTime, info, MedicationStatus.TAKEN, alertTime.getTimeToTake())
		);
	}

	@Test
	@DisplayName("복약 스케줄 등록")
	void registerMedication() throws Exception {
		MedicationScheduleRequest request = MedicationScheduleRequest.builder()
			.patientGuardianId(1L)
			.medicationName("타이레놀")
			.timeToTake(LocalDateTime.now().plusHours(2))
			.days(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))
			.build();

		mockMvc.perform(post("/api/v1/medications")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("patientGuardianId").description("환자-보호자 관계 ID"),
					fieldWithPath("medicationName").description("약 이름"),
					fieldWithPath("timeToTake").description("복약 시간 (ISO-8601 형식)"),
					fieldWithPath("days").description("복약 요일 리스트 (예: MONDAY, WEDNESDAY)")
				)
			));
	}

	@Test
	@DisplayName("복약 로그 조회")
	void getMedicationLogs() throws Exception {
		mockMvc.perform(get("/api/v1/medications/patients/{userId}", 1L))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("userId").description("보호자 ID (또는 patientGuardianId)")
				),
				responseFields(
					fieldWithPath("[].medicationLogId").description("복약 로그 ID"),
					fieldWithPath("[].medicationId").description("약 ID"),
					fieldWithPath("[].status").description("복약 상태 (TAKEN, MISSED)"),
					fieldWithPath("[].timeToTake").description("복약 시간")
				)
			));
	}

	@Test
	@DisplayName("복약 완료 처리")
	void completeMedication() throws Exception {
		MedicationCompleteRequest request = MedicationCompleteRequest.builder()
			.status(MedicationStatus.TAKEN)
			.build();

		mockMvc.perform(patch("/api/v1/medications/{medicationId}/complete", savedMedicationId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("medicationId").description("약 ID")
				),
				requestFields(
					fieldWithPath("status").description("복약 상태 (TAKEN, MISSED)")
				)
			));
	}

	@Test
	@DisplayName("복약 시간 수정")
	void updateMedicationTime() throws Exception {
		MedicationUpdateRequest request = MedicationUpdateRequest.builder()
			.newTimeToTake(LocalDateTime.now().plusHours(3))
			.build();

		mockMvc.perform(patch("/api/v1/medications/{medicationId}", savedMedicationId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("medicationId").description("약 ID")
				),
				requestFields(
					fieldWithPath("newTimeToTake").description("변경할 복약 시간 (ISO-8601 형식)")
				)
			));
	}

	@Test
	@DisplayName("복약 스케줄 삭제")
	void deleteMedicationSchedule() throws Exception {
		mockMvc.perform(delete("/api/v1/medications/{medicationId}", savedMedicationId))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("medicationId").description("삭제할 약 ID")
				)
			));
	}
}
