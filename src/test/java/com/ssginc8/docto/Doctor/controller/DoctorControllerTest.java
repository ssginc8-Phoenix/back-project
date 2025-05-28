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
			.hospitalId(22L)
			.specialization(Specialization.valueOf("DERMATOLOGY"))
			.username("dr.jjh")
			.password("asd123")
			.email("jjh1@naver.com")
			.login_type("EMAIL")
			.role("DOCTOR")
			.suspended(false)
			.uuid("asd123123111111")
			.build();

		mockMvc.perform(post("/api/v1/doctors")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorSaveRequest)))
			.andExpect(status().isOk()) // 실제 상황에 따라 isCreated() 등으로 변경 가능
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("hospitalId").type(JsonFieldType.NUMBER).description("병원ID"),
					fieldWithPath("specialization").type(JsonFieldType.STRING).description("전공"),
					fieldWithPath("username").type(JsonFieldType.STRING).description("이름"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
					fieldWithPath("email").type(JsonFieldType.STRING).optional().description("이메일"),
					fieldWithPath("login_type").type(JsonFieldType.STRING).optional().description("로그인타입"),
					fieldWithPath("role").type(JsonFieldType.STRING).description("역할"),
					fieldWithPath("suspended").type(JsonFieldType.BOOLEAN).description("정지 여부"),
					fieldWithPath("uuid").type(JsonFieldType.STRING).optional().description("UUID")



					)
			));

	}

	@Test
	@DisplayName("의사 정보 수정")
	void updateDoctorTest() throws Exception {
		DoctorUpdateRequest doctorUpdateRequest = DoctorUpdateRequest.builder()
			.email("avbv@naver.com")
			.password("1qa12a")
			.specialization(Specialization.valueOf("PSYCHIATRY"))
			.build();

		mockMvc.perform(patch("/api/v1/doctors/{doctorId}",9L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(doctorUpdateRequest)))
			.andExpect(status().isOk()) // 실제 상황에 따라 isCreated() 등으로 변경 가능
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("doctorId").description("의사 ID")
				),
				requestFields(
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
					fieldWithPath("email").type(JsonFieldType.STRING).optional().description("이메일"),
					fieldWithPath("specialization").type(JsonFieldType.STRING).description("전공")



				)
			));
	}

	@Test
	@DisplayName("의사 전체 조회")
	void getAllDoctorsTest() throws Exception {
		mockMvc.perform(get("/api/v1/doctors")
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
		Long hospitalId = 22L;

		mockMvc.perform(get("/api/v1/doctors")
				.param("hospitalId", String.valueOf(hospitalId))
				.param("page", "0")
				.param("size", "10")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				queryParameters(
					parameterWithName("hospitalId").description("병원 ID"),
					parameterWithName("page").optional().description("페이지 번호 (0부터 시작)").attributes(
						key("defaultValue").value("0")),
					parameterWithName("size").optional().description("페이지당 항목 수").attributes(
						key("defaultValue").value("20"))
				),
				responseFields(
					fieldWithPath("content[].doctorId").type(JsonFieldType.NUMBER).description("의사 ID"),
					fieldWithPath("content[].specialization").type(JsonFieldType.STRING).description("전공"),
					fieldWithPath("content[].username").type(JsonFieldType.STRING).description("의사 이름"),
					fieldWithPath("content[].hospitalId").type(JsonFieldType.NUMBER).description("병원 ID"),

					// 페이징 정보
					subsectionWithPath("pageable").ignored(),
					fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
					fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 요소 수"),
					fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
					fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지당 항목 수"),
					fieldWithPath("number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
					fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 조건 없음 여부"),
					fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬됨 여부"),
					fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬되지 않음 여부"),
					fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
					fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지 요소 수"),
					fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("페이지가 비어 있는지 여부")
				)
			));
	}

	@Test
	@DisplayName("의사 영업시간 등록")
	void saveDoctorSchedule() throws Exception {
		Long doctorId = 9L;

		List<DoctorScheduleRequest> schedules = List.of(
			DoctorScheduleRequest.builder()
				.dayOfWeek(DayOfWeek.MONDAY)
				.startTime(LocalTime.parse("09:00"))
				.endTime(LocalTime.parse("18:00"))
				.lunchStart(LocalTime.parse("12:00"))
				.lunchEnd(LocalTime.parse("13:00"))
				.build(),
			DoctorScheduleRequest.builder()
				.dayOfWeek(DayOfWeek.TUESDAY)
				.startTime(LocalTime.parse("09:00"))
				.endTime(LocalTime.parse("18:00"))
				.lunchStart(LocalTime.parse("12:00"))
				.lunchEnd(LocalTime.parse("13:00"))
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
		Long doctorId = 9L;

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
		Long doctorId = 9L;
		Long scheduleId = 4L;

		DoctorScheduleRequest updateRequest = DoctorScheduleRequest.builder()
			.dayOfWeek(DayOfWeek.MONDAY)
			.startTime(LocalTime.parse("10:00"))
			.endTime(LocalTime.parse("19:00"))
			.lunchStart(LocalTime.parse("13:00"))
			.lunchEnd(LocalTime.parse("14:00"))
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
				),
				responseFields(
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
		Long doctorId = 9L;
		Long scheduleId = 4L;

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


