package com.ssginc8.docto.Doctor.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ssginc8.docto.doctor.dto.DoctorSaveRequest;

import com.ssginc8.docto.doctor.dto.DoctorScheduleRequest;
import com.ssginc8.docto.doctor.dto.DoctorUpdateRequest;
import com.ssginc8.docto.doctor.entity.Specialization;
import com.ssginc8.docto.restdocs.RestDocsConfig;

@ActiveProfiles("prod")
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@Import(RestDocsConfig.class)
public class DoctorControllerTest {


	@Autowired
	protected RestDocumentationResultHandler restDocs;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation))
			.alwaysDo(MockMvcResultHandlers.print())
			.alwaysDo(restDocs)
			.addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 깨짐 방지
			.build();

	}



	@Test
	@DisplayName("의사 등록")
	void saveDoctorTest() throws Exception {
		DoctorSaveRequest doctorSaveRequest = DoctorSaveRequest.builder()
			.hospitalId(3L)
			.specialization(Specialization.valueOf("DERMATOLOGY"))
			.userId(8L)
			.build();

		mockMvc.perform(post("/api/v1/doctors")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorSaveRequest)))
			.andExpect(status().isOk()) // 실제 상황에 따라 isCreated() 등으로 변경 가능
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("hospitalId").type(JsonFieldType.NUMBER).description("병원 ID"),
					fieldWithPath("specialization").type(JsonFieldType.STRING).description("전공 과목"),
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 ID")



					)
			));

	}


	@Test
	@DisplayName("의사 전체 조회")
	void getAllDoctorsTest() throws Exception {
		mockMvc.perform(get("/api/v1/admin/doctors")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				queryParameters(
					parameterWithName("page")
						.attributes(
							key("constraints").value("0 이상의 정수"),
							key("defaultValue").value("0"))
						.description("Optional, 페이지 번호 (0부터 시작)").optional(),
					parameterWithName("size")
						.attributes(
							key("constraints").value("1 이상의 정수"),
							key("defaultValue").value("10"))
						.description("Optional, 페이지당 항목 수").optional()
				)

			));
	}

	@Test
	@DisplayName("병원에 속한 의사 조회")
	void getDoctorByHospitalTest() throws Exception {
		mockMvc.perform(get("/api/v1/doctors")
				.param("hospitalId", "1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				queryParameters(
					parameterWithName("hospitalId").description("병원 ID")
				),
				responseFields(
					fieldWithPath("[].doctorId").type(JsonFieldType.NUMBER).description("의사 ID"),
					fieldWithPath("[].specialization").type(JsonFieldType.STRING).description("전공"),
					fieldWithPath("[].username").type(JsonFieldType.STRING).description("의사 이름"),
					fieldWithPath("[].hospitalName").type(JsonFieldType.STRING).description("병원 이름")
				)
			));
	}



	@Test
	@DisplayName("의사 영업시간 등록")
	void saveDoctorSchedule() throws Exception {
		Long doctorId = 6L;

		List<DoctorScheduleRequest> schedules = List.of(
			DoctorScheduleRequest.builder()
				.dayOfWeek(DayOfWeek.MONDAY)
				.startTime(LocalTime.parse("09:00"))
				.endTime(LocalTime.parse("18:00"))
				.lunchStart(LocalTime.parse("00:00"))
				.lunchEnd(LocalTime.parse("00:00"))
				.build(),
			DoctorScheduleRequest.builder()
				.dayOfWeek(DayOfWeek.TUESDAY)
				.startTime(LocalTime.parse("09:00"))
				.endTime(LocalTime.parse("18:00"))
				.lunchStart(LocalTime.parse("00:00"))
				.lunchEnd(LocalTime.parse("00:00"))
				.build()
		);

		mockMvc.perform(post("/api/v1/doctors/{doctorId}/schedules", doctorId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(schedules)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("doctorId").description("의사 ID")
				),
				requestFields(
					fieldWithPath("[].dayOfWeek").description("요일"),
					fieldWithPath("[].startTime").description("진료 시작 시간"),
					fieldWithPath("[].endTime").description("진료 종료 시간"),
					fieldWithPath("[].lunchStart").description("점심 시작 시간"),
					fieldWithPath("[].lunchEnd").description("점심 종료 시간")
				)
			));

	}


	@Test
	@DisplayName("의사 영업시간 조회")
	void getDoctorSchedule() throws Exception {
		Long doctorId = 6L;

		mockMvc.perform(get("/api/v1/doctors/{doctorId}/schedules", doctorId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("doctorId").description("의사 ID")
				),
				responseFields(
					fieldWithPath("[].scheduleId").description("스케줄 ID"),
					fieldWithPath("[].doctorId").description("의사 ID"),
					fieldWithPath("[].dayOfWeek").description("요일"),
					fieldWithPath("[].startTime").description("영업 시작 시간"),
					fieldWithPath("[].endTime").description("영업 종료 시간"),
					fieldWithPath("[].lunchStart").description("점심 시작 시간"),
					fieldWithPath("[].lunchEnd").description("점심 종료 시간")
				)
			));
	}

	@Test
	@DisplayName("의사 영업시간 수정")
	void updateDoctorSchedule() throws Exception {
		Long doctorId = 6L;
		Long scheduleId = 9L;

		DoctorScheduleRequest updateRequest = DoctorScheduleRequest.builder()
			.dayOfWeek(DayOfWeek.MONDAY)
			.startTime(LocalTime.parse("10:00"))
			.endTime(LocalTime.parse("19:00"))
			.lunchStart(LocalTime.parse("00:00"))
			.lunchEnd(LocalTime.parse("00:00"))
			.build();

		mockMvc.perform(patch("/api/v1/doctors/{doctorId}/schedules/{scheduleId}", doctorId, scheduleId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("doctorId").description("의사 ID"),
					parameterWithName("scheduleId").description("수정할 스케줄 ID")
				),
				requestFields(
					fieldWithPath("dayOfWeek").description("요일"),
					fieldWithPath("startTime").description("영업 시작 시간"),
					fieldWithPath("endTime").description("영업 종료 시간"),
					fieldWithPath("lunchStart").description("점심 시작 시간"),
					fieldWithPath("lunchEnd").description("점심 종료 시간")
				)
			));
	}


	@Test
	@DisplayName("의사 영업시간 삭제")
	void deleteDoctorSchedule() throws Exception {
		Long doctorId = 6L;
		Long scheduleId = 10L;

		mockMvc.perform(delete("/api/v1/doctors/{doctorId}/schedules/{scheduleId}", doctorId, scheduleId))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("doctorId").description("의사 ID"),
					parameterWithName("scheduleId").description("삭제할 스케줄 ID")
				)
			));
	}
}


