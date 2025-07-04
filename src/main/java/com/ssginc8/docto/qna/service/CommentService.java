package com.ssginc8.docto.qna.service;

import com.ssginc8.docto.qna.dto.CommentResponse;

import java.util.List;


public interface CommentService {

	// 답변 작성
	CommentResponse createComment(Long qnaPostId, String content);

	// 게시글에 달린 댓글 조회
	List<CommentResponse> getCommentsByPost(Long qnaPostId);

	// 댓글 수정
	CommentResponse updateComment(Long commentId, String content);

	// 댓글 조회
	CommentResponse getComment(Long commentId);

	// 댓글 삭제
	void deleteComment(Long commentId);
}
