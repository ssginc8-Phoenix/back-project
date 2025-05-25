package com.ssginc8.docto.cs.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssginc8.docto.cs.dto.AssignAgentRequest;
import com.ssginc8.docto.cs.dto.CsMessageRequest;
import com.ssginc8.docto.cs.dto.CsRoomCreateRequest;
import com.ssginc8.docto.cs.dto.UpdateStatusRequest;
import com.ssginc8.docto.restdocs.RestDocsConfig;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@Import(RestDocsConfig.class)
public class CsControllerTest {

	@Autowired
	protected RestDocumentationResultHandler restDocs;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PathPatternParser pathPatternParser;

	@BeforeEach
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation))
			.alwaysDo(print())
			.alwaysDo(restDocs)
			.addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 깨짐 방지
			.build();
	}

	@Test
	@DisplayName("채팅방 생성 테스트")
	void createCsRoom() throws Exception {
		CsRoomCreateRequest request = CsRoomCreateRequest.builder()
			.customerId(1L)
			.build();

		mockMvc.perform(post("/api/v1/csrooms")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("customerId").type(JsonFieldType.NUMBER).description("고객 ID")
				)
			));
	}

	@Test
	@DisplayName("WAITING 상태인 채팅방 상담사 배정")
	void assignAgent() throws Exception {
		AssignAgentRequest request = AssignAgentRequest.builder()
			.agentId(5L)
			.build();

		mockMvc.perform(patch("/api/v1/csrooms/{csRoomId}/assign", 3L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("csRoomId").description("CS 채팅방 ID")
				),
				requestFields(
					fieldWithPath("agentId").type(JsonFieldType.NUMBER).description("상담사 ID")
				)
			));
	}

	@Test
	@DisplayName("채팅방 상세 조회 테스트")
	void getCsRoomDetail() throws Exception {
		mockMvc.perform(get("/api/v1/csRooms/{csRoomId}", 4L))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("csRoomId").description("CS 채팅방 ID")
				),
				responseFields(
					fieldWithPath("csRoomId").type(JsonFieldType.NUMBER).description("CS 채팅방 ID"),
					fieldWithPath("customerId").type(JsonFieldType.NUMBER).description("고객 ID"),
					fieldWithPath("agentId").type(JsonFieldType.NUMBER).description("상담사 ID").optional(),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태 (WAITING, OPEN, CLOSE)")
				)
			));
	}

	@Test
	@DisplayName("채팅방 리스트 조회 테스트")
	void getCsRoomList() throws Exception {
		mockMvc.perform(get("/api/v1/admin/csrooms")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				queryParameters(
					parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
					parameterWithName("size").description("페이지 당 데이터 개수").optional(),
					parameterWithName("sort").description("정렬 기준 (예: createdAt,desc)").optional()
				)
			));
	}

	@Test
	@DisplayName("상담 상태 변경")
	void changeStatus() throws Exception {
		UpdateStatusRequest request = UpdateStatusRequest.builder()
			.status("CLOSED")
			.build();

		mockMvc.perform(patch("/api/v1/csrooms/{csRoomId}/status", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("csRoomId").description("CS 채팅방 ID")
				),
				requestFields(
					fieldWithPath("status").description("변경할 상담 상태 (WAITING, OPEN, CLOSE)")
				)
			));
	}

	@Test
	@DisplayName("CS 채팅방 삭제")
	void deleteCsRoom() throws Exception {
		mockMvc.perform(delete("/api/v1/csrooms/{csRoomId}", 1L))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("csRoomId").description("삭제할 CS 채팅방 ID")
				)
			));
	}

	@Test
	@DisplayName("CS 메시지 전송")
	void createMessage() throws Exception {
		CsMessageRequest request = CsMessageRequest.builder()
			.userId(1L)
			.content("안녕하세요. 질문이 있어요.")
			.build();

		mockMvc.perform(post("/api/v1/csrooms/{csRoomId}/messages", 4L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("csRoomId").description("CS 채팅방 ID")
				),
				requestFields(
					fieldWithPath("userId").description("보내는 사람 ID"),
					fieldWithPath("content").description("내용")
				)
			));
	}


	@Test
	@DisplayName("CS 채팅방 메시지 조회 (무한 스크롤)")
	void getMessages() throws Exception {
		Long csRoomId = 4L;
		String before = "2025-12-31T23:59:59"; // ISO-8601 형식
		int size = 2;

		mockMvc.perform(get("/api/v1/csrooms/{csRoomId}/messages", csRoomId)
				.param("before", before)
				.param("size", String.valueOf(size))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("csRoomId").description("CS 채팅방 ID")
				),
				queryParameters(
					parameterWithName("before").optional().description("이 시간 이전 메시지를 조회 (기본: 최신순)"),
					parameterWithName("size").optional().description("가져올 메시지 개수 (기본: 20)")
				),
				responseFields(
					fieldWithPath("[].csMessageId").type(JsonFieldType.NUMBER).description("메시지 ID"),
					fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("보낸 사람 ID"),
					fieldWithPath("[].content").type(JsonFieldType.STRING).description("메시지 내용"),
					fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("메시지 전송 시각 (ISO-8601)")
				)
		));
	}
}
