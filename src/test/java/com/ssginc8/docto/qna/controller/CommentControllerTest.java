package com.ssginc8.docto.qna.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssginc8.docto.qna.dto.CommentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("prod")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;


	@Test
	@DisplayName("의사 답변 생성")
	@WithMockUser(username = "guardianUser", roles = {"GUARDIAN"})
	void createQaComment() throws Exception {
		CommentRequest req = new CommentRequest();
		req.setContent("답글 내용 예시");

		mockMvc.perform(post("/api/v1/qnas/{qnaId}/comments", 14L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
			.andExpect(status().isOk())

			.andExpect(jsonPath("$.commentId").isNumber())
			.andExpect(jsonPath("$.qnaPostId").value(14))
			.andExpect(jsonPath("$.content").value("답글 내용 예시"))
			.andDo(document("comment-controller-test/create-qa-comment",
				pathParameters(
					parameterWithName("qnaId").description("Q&A 게시글 ID")
				),
				requestFields(

					fieldWithPath("content").type(JsonFieldType.STRING).description("답변 본문")
				),
				responseFields(

					fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("생성된 답변 ID"),
					fieldWithPath("qnaPostId").type(JsonFieldType.NUMBER).description("연결된 Q&A 게시글 ID"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("답변 내용"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시각"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).optional().description("수정 시각 (없을 수도 있음)")
				)
			));
	}

	@Test
	@DisplayName("답변 리스트 조회")
	@WithMockUser(username = "guardianUser", roles = {"GUARDIAN"})
	void listComments() throws Exception {
		mockMvc.perform(get("/api/v1/qnas/{qnaId}/comments", 14L)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andDo(document("comment-controller-test/get-qa-comments",
				pathParameters(
					parameterWithName("qnaId").description("Q&A 게시글 ID")
				),
				responseFields(
					fieldWithPath("[].commentId").type(JsonFieldType.NUMBER).description("답변 ID"),
					fieldWithPath("[].qnaPostId").type(JsonFieldType.NUMBER).description("연결된 Q&A 게시글 ID"),
					fieldWithPath("[].content").type(JsonFieldType.STRING).description("답변 내용"),
					fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("생성 시각 (ISO-8601)"),
					fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("수정 시각 (ISO-8601)")
				)
			));
	}

	@Test
	@DisplayName("답변 단건 댓글 조회")
	@WithMockUser(username = "guardianUser", roles = {"GUARDIAN"})
	void getComment() throws Exception {
		mockMvc.perform(get("/api/v1/qnas/comments/{commentId}", 18L)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.commentId").value(18))
			.andDo(document("comment-controller-test/get-qa-comment",
				pathParameters(
					parameterWithName("commentId").description("답변 ID")
				),
				responseFields(
					fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("답변 ID"),
					fieldWithPath("qnaPostId").type(JsonFieldType.NUMBER).description("연결된 Q&A 게시글 ID"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("답변 내용"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시각 (ISO-8601)"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정 시각 (ISO-8601)")
				)
			));
	}


	@Test
	@DisplayName("의사 답변 수정")
	@WithMockUser(username = "guardianUser", roles = {"GUARDIAN"})
	void updateComment() throws Exception {
		CommentRequest req = new CommentRequest();
		req.setContent("수정된 답변 내용");

		mockMvc.perform(patch("/api/v1/qnas/comments/{commentId}", 18L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.commentId").value(18))
			.andExpect(jsonPath("$.content").value("수정된 답변 내용"))
			.andDo(document("comment-controller-test/update-qa-comment",
				pathParameters(
					parameterWithName("commentId").description("답변 ID")
				),
				requestFields(
					fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 답변 본문")
				),
				responseFields(
					fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("답변 ID"),
					fieldWithPath("qnaPostId").type(JsonFieldType.NUMBER).description("연결된 Q&A 게시글 ID"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("수정된 답변 내용"),
					fieldWithPath("createdAt").type(JsonFieldType.STRING).description("원본 생성 시각 (ISO-8601)"),
					fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("최종 수정 시각 (ISO-8601)")
				)
			));
	}

	@Test
	@DisplayName("의사 답변 삭제")
	@WithMockUser(username = "guardianUser", roles = {"GUARDIAN"})
	void deleteComment() throws Exception {
		mockMvc.perform(delete("/api/v1/qnas/comments/{commentId}", 16L))
			.andExpect(status().isNoContent())
			.andDo(document("comment-controller-test/delete-qa-comment",
				pathParameters(
					parameterWithName("commentId").description("답변 ID")
				)
			));
	}
}
