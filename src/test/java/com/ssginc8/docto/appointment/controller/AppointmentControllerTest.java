package com.ssginc8.docto.appointment.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssginc8.docto.appointment.dto.AppointmentRequest;
import com.ssginc8.docto.appointment.dto.RescheduleRequest;
import com.ssginc8.docto.appointment.dto.UpdateRequest;

@SpringBootTest
public class AppointmentControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 깨짐 방지
			.build();
	}

	@Test
	@DisplayName("예약 접수 테스트")
	void requestAppointment() throws Exception {
		AppointmentRequest request = AppointmentRequest.builder()
			.userId(2L)
			.patientId(1L)
			.hospitalId(1L)
			.doctorId(1L)
			.symptom("기침과 열")
			.question("감기일까요?")
			.appointmentType("SCHEDULED")
			.paymentType("ONSITE")
			.appointmentTime(LocalDateTime.now().plusDays(1))
			.build();

		mockMvc.perform(post("/api/v1/appointments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("예약 상세 조회")
	void getAppointmentDetail() throws Exception {
		mockMvc.perform(get("/api/v1/appointments/{id}", 1L))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("예약 리스트 조회")
	void getAppointmentList() throws Exception {
		mockMvc.perform(get("/api/v1/appointments")
			.param("userId", "1")
			.param("page", "0")
			.param("size", "5"))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("예약 상태 변경")
	void updateAppointmentStatus() throws Exception {
		UpdateRequest request = new UpdateRequest();
		request.setStatus("CONFIRMED");

		mockMvc.perform(patch("/api/v1/appointments/{id}/status", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("재예약")
	void rescheduleAppointment() throws Exception {
		RescheduleRequest request = new RescheduleRequest();
		request.setNewTime(LocalDateTime.now().plusDays(2));

		mockMvc.perform(post("/api/v1/appointments/{id}/reschedule", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}
}
