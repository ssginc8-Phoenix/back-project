package com.ssginc8.docto.hospital.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssginc8.docto.hospital.dto.HospitalRequest;
import com.ssginc8.docto.hospital.dto.HospitalScheduleRequest;

import com.ssginc8.docto.hospital.dto.HospitalUpdate;
import com.ssginc8.docto.hospital.dto.HospitalWaitingRequest;
import com.ssginc8.docto.restdocs.RestDocsConfig;

@ActiveProfiles("prod")
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@Import(RestDocsConfig.class)
public class HospitalControllerTest {

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
	@DisplayName("병원 등록 API 테스트")
	void saveHospitalTest() throws Exception {
		HospitalRequest request = HospitalRequest.builder()
			.userId(6L)
			.name("삼성병원")
			.address("서울특별시 강남구")
			.phone("010-1234-5678")
			.introduction("최신 장비를 갖춘 병원입니다.")
			.notice("공휴일은 휴진입니다.")
			.businessRegistrationNumber("123-45-67890")
			.serviceName(List.of("주차 가능", "야간 진료", "여의사 진료"))
			.latitude(new BigDecimal("37.5665"))
			.longitude(new BigDecimal("126.9780"))
			.build();

		mockMvc.perform(post("/api/v1/hospitals")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk()) // 실제 상황에 따라 isCreated() 등으로 변경 가능
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 ID"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("병원 이름"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("병원 주소"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("병원 전화번호"),
					fieldWithPath("introduction").type(JsonFieldType.STRING).optional().description("병원 소개"),
					fieldWithPath("notice").type(JsonFieldType.STRING).optional().description("병원 공지사항"),
					fieldWithPath("businessRegistrationNumber").type(JsonFieldType.STRING).description("사업자등록번호"),
					fieldWithPath("serviceName").type(JsonFieldType.ARRAY).description("제공 서비스 목록"),
					fieldWithPath("latitude").type(JsonFieldType.NUMBER).optional().description("위도"),
					fieldWithPath("longitude").type(JsonFieldType.NUMBER).optional().description("경도")

				)
			));
	}

	@Test
	@DisplayName("병원 상세 조회 테스트")
	void getHospitalById() throws Exception {
		Long hospitalId = 3L;

		mockMvc.perform(get("/api/v1/hospitals/{hospitalId}", hospitalId))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("hospitalId").description("조회할 병원의 ID")
				),
				responseFields(
					fieldWithPath("hospitalId").type(JsonFieldType.NUMBER).description("병원 ID"),
					fieldWithPath("name").type(JsonFieldType.STRING).description("병원 이름"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("병원 주소"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
					fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개").optional(),
					fieldWithPath("notice").type(JsonFieldType.STRING).description("공지사항").optional(),
					fieldWithPath("serviceNames").type(JsonFieldType.ARRAY).description("제공 서비스 목록"),
					fieldWithPath("latitude").type(JsonFieldType.NUMBER).description("위도"),
					fieldWithPath("longitude").type(JsonFieldType.NUMBER).description("경도"),
					fieldWithPath("waiting").type(JsonFieldType.NUMBER).optional() .description("대기 인원 수")

				)
			));
	}

	@Test
	@DisplayName("병원 정보 수정")
	void updateHospital() throws Exception {
		HospitalUpdate request = HospitalUpdate.builder()
			.name("5월병원")
			.address("서울특별시 강남구")
			.phone("010-1234-5678")
			.introduction("최신 장비를 갖춘 병원입니다.")
			.notice("공휴일은 휴진입니다.")
			.businessRegistrationNumber("123-45-67890")
			.serviceNames(List.of("의사많음", "깔끔"))
			.build();

		mockMvc.perform(patch("/api/v1/hospitals/{hospitalId}", 3L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("hospitalId").description("병원 ID")
				),
				requestFields(
					fieldWithPath("name").type(JsonFieldType.STRING).description("병원 이름"),
					fieldWithPath("address").type(JsonFieldType.STRING).description("병원 주소"),
					fieldWithPath("phone").type(JsonFieldType.STRING).description("전화번호"),
					fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개").optional(),
					fieldWithPath("notice").type(JsonFieldType.STRING).description("공지사항").optional(),
					fieldWithPath("businessRegistrationNumber").type(JsonFieldType.STRING).description("사업자등록번호"),
					fieldWithPath("serviceNames").type(JsonFieldType.ARRAY).description("제공 서비스 목록")


				)
			));
	}
	@Test
	@DisplayName("병원 삭제")
	void deleteHospital() throws Exception {
		mockMvc.perform(delete("/api/v1/hospitals/{hospitalId}", 5L))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("hospitalId").description("삭제할 병원 ID (관련 스케쥴 및 의사도 함께 삭제됨)")
				)

			));
	}

	@Test
	@DisplayName("병원 전체 리스트 조회 (어드민)")
	void getAllHospitals() throws Exception {
		mockMvc.perform(get("/api/v1/admin/hospitals")
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
	@DisplayName("병원 영업시간 조회")
	void getSchedules() throws Exception {
		Long hospitalId = 1L;

		mockMvc.perform(get("/api/v1/hospitals/{hospitalId}/schedules", hospitalId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("hospitalId").description("병원 ID")
				),
				responseFields(
					fieldWithPath("[].hospitalScheduleId").description("스케줄 ID"),
					fieldWithPath("[].dayOfWeek").description("요일"),
					fieldWithPath("[].openTime").description("영업 시작 시간"),
					fieldWithPath("[].closeTime").description("영업 종료 시간"),
					fieldWithPath("[].lunchStart").description("점심 시작 시간"),
					fieldWithPath("[].lunchEnd").description("점심 종료 시간")

				)
			));
	}
	@Test
	@DisplayName("병원 영업시간 등록")
	void saveSchedule() throws Exception {
		Long hospitalId = 3L;

		List<HospitalScheduleRequest> schedules = List.of(
			HospitalScheduleRequest.builder()
				.dayOfWeek(DayOfWeek.MONDAY)
				.openTime(LocalTime.parse("09:00"))
				.closeTime(LocalTime.parse("18:00"))
				.lunchStart(LocalTime.parse("12:00"))
				.lunchEnd(LocalTime.parse("13:00"))
				.build(),
			HospitalScheduleRequest.builder()
				.dayOfWeek(DayOfWeek.TUESDAY)
				.openTime(LocalTime.parse("09:00"))
				.closeTime(LocalTime.parse("18:00"))
				.lunchStart(LocalTime.parse("12:00"))
				.lunchEnd(LocalTime.parse("13:00"))
				.build()
		);

		mockMvc.perform(post("/api/v1/hospitals/{hospitalId}/schedules", hospitalId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(schedules)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("hospitalId").description("병원 ID")
				),
				requestFields(
					fieldWithPath("[].dayOfWeek").description("요일"),
					fieldWithPath("[].openTime").description("영업 시작 시간"),
					fieldWithPath("[].closeTime").description("영업 종료 시간"),
					fieldWithPath("[].lunchStart").description("점심 시작 시간"),
					fieldWithPath("[].lunchEnd").description("점심 종료 시간")
				)
			));
	}

	@Test
	@DisplayName("병원 영업시간 삭제")
	void deleteSchedule() throws Exception {
		Long hospitalId = 3L;
		Long scheduleId = 24L;

		mockMvc.perform(delete("/api/v1/hospitals/{hospitalId}/schedules/{scheduleId}", hospitalId, scheduleId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("hospitalId").description("병원 ID"),
					parameterWithName("scheduleId").description("스케줄 ID")
				)
			));
	}

	@Test
	@DisplayName("병원 영업시간 수정")
	void updateSchedule() throws Exception {
		Long hospitalId = 3L;
		Long scheduleId = 23L;

		HospitalScheduleRequest scheduleRequest = HospitalScheduleRequest.builder()
			.dayOfWeek(DayOfWeek.WEDNESDAY)
			.openTime(LocalTime.parse("08:30"))
			.closeTime(LocalTime.parse("17:30"))
			.lunchStart(LocalTime.parse("12:00"))
			.lunchEnd(LocalTime.parse("13:00"))
			.build();

		mockMvc.perform(patch("/api/v1/hospitals/{hospitalId}/schedules/{scheduleId}", hospitalId, scheduleId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(scheduleRequest)))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("hospitalId").description("병원 ID"),
					parameterWithName("scheduleId").description("스케줄 ID")
				),
				requestFields(
					fieldWithPath("dayOfWeek").description("요일"),
					fieldWithPath("openTime").description("영업 시작 시간"),
					fieldWithPath("closeTime").description("영업 종료 시간"),
					fieldWithPath("lunchStart").description("점심 시작 시간"),
					fieldWithPath("lunchEnd").description("점심 종료 시간")
				)
			));
	}

	@Test
	@DisplayName("병원 웨이팅 등록")
	void saveHospitalWaiting() throws Exception {
		Long hospitalId = 3L;

		HospitalWaitingRequest request = new HospitalWaitingRequest(5L); // DTO 사용

		mockMvc.perform(post("/api/v1/hospitals/{hospitalId}/waiting", hospitalId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("hospitalId").description("병원 ID")
				),
				requestFields(
					fieldWithPath("waiting").description("대기 인원 수")
				),
				responseFields(
					fieldWithPath("waiting").description("등록된 대기 인원 수") // 혹은 필요 시 .type(JsonFieldType.NUMBER)
				)
			));
	}


	@Test
	@DisplayName("병원 웨이팅 조회")
	void getHospitalWaiting() throws Exception {
		Long hospitalId = 3L;

		mockMvc.perform(get("/api/v1/hospitals/{hospitalId}/waiting", hospitalId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("hospitalId").description("병원 ID")
				),
				responseBody()
			));
	}

	@Test
	@DisplayName("병원 웨이팅 수정")
	void updateHospitalWaiting() throws Exception {
		Long hospitalId = 3L;
		Map<String, Long> request = Map.of("waiting", 3L);

		mockMvc.perform(patch("/api/v1/hospitals/{hospitalId}/waiting", hospitalId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("hospitalId").description("병원 ID")
				),
				requestFields(
					fieldWithPath("waiting").description("수정할 대기 인원 수")
				)
			));
	}

	@Test
	@DisplayName("병원에서 리뷰 조회")
	void getAllReviews() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/hospitals/{hospitalId}/reviews", 1L)
				.param("page", "0")
				.param("size", "5"))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("hospitalId")
						.attributes(key("constraints").value("숫자 입력"))
						.description("Required, 병원 ID")
				),
				queryParameters(
					parameterWithName("page")
						.attributes(
							key("constraints").value("0 이상의 정수"),
							key("defaultValue").value("0"))
						.description("Optional, 페이지 번호 (0부터 시작)").optional(),
					parameterWithName("size")
						.attributes(
							key("constraints").value("1 이상의 정수"),
							key("defaultValue").value("5"))
						.description("Optional, 페이지당 항목 수").optional()
				)
			));
	}



}
