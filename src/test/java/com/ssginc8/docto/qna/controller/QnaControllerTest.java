package com.ssginc8.docto.qna.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssginc8.docto.restdocs.RestDocsConfig;
import com.ssginc8.docto.qna.dto.QaPostCreateRequest;
import com.ssginc8.docto.qna.dto.QaPostUpdateRequest;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;


@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@Import(RestDocsConfig.class)
public class QnaControllerTest {

	@Autowired
	protected RestDocumentationResultHandler restDocs;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;


	@BeforeEach
	public void setUp(RestDocumentationContextProvider restDocsProvider) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocsProvider))
			.alwaysDo(restDocs)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();
	}


	@Test
	@DisplayName("게시물 생성")
	void createQaPost() throws Exception {
		QaPostCreateRequest request = new QaPostCreateRequest(3L, "문의 내용 예시");
		mockMvc.perform(post("/api/v1/qnas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.qnaPostId").isNumber())
			.andExpect(jsonPath("$.appointmentId").value(3))
			.andExpect(jsonPath("$.content").value("문의 내용 예시"))
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("appointmentId").description("예약 ID"),
					fieldWithPath("content").description("게시글 본문")
				),
				responseFields(
					fieldWithPath("qnaPostId").type(JsonFieldType.NUMBER).description("생성된 Q&A 게시글 ID"),
					fieldWithPath("appointmentId").type(JsonFieldType.NUMBER).description("예약 ID"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시각"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정(최종 변경) 시각")
				)
			));
	}


	@Test
	@DisplayName("게시글 상세 조회")
	void getQaPost() throws Exception {
		mockMvc.perform(get("/api/v1/qnas/{qnaId}", 12L)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("qnaId")
						.attributes(key("constraints").value("숫자 입력"))
						.description("Q&A 게시글 ID")
				),
				responseFields(
					fieldWithPath("qnaPostId").type(JsonFieldType.NUMBER).description("Q&A 게시글 Id"),
					fieldWithPath("appointmentId").type(JsonFieldType.NUMBER).description("예약 ID"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시각"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정 시각")
				)
			));
	}


	@Test
	@DisplayName("게시글 수정")
	void updateQaPost() throws Exception {

		QaPostUpdateRequest req = new QaPostUpdateRequest( "수정된 내용");

		mockMvc.perform(patch("/api/v1/qnas/{qnaId}", 12L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.qnaPostId").value(12))
			.andExpect(jsonPath("$.appointmentId").value(3))
			.andExpect(jsonPath("$.content").value("수정된 내용"))
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("qnaId")
						.attributes(key("constraints").value("숫자 입력"))
						.description("수정할 Q&A 게시글 ID")
				),
				requestFields(
					fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 게시글 본문")
				),
				responseFields(
					fieldWithPath("qnaPostId").type(JsonFieldType.NUMBER).description("Q&A 게시글 ID"),
					fieldWithPath("appointmentId").type(JsonFieldType.NUMBER).description("연결된 예약 ID"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 게시글 본문"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("원본 생성 시각 (ISO-8601)"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("최종 수정 시각 (ISO-8601)")
				)
			));
	}



	@Test
	@DisplayName("게시글 삭제")
	void deleteQaPost() throws Exception {
		mockMvc.perform(delete("/api/v1/qnas/{qnaId}", 12L))
			.andExpect(status().isNoContent())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("qnaId").description("삭제할 Q&A 게시글 ID")
				),
				httpResponse()
			));
	}
}
