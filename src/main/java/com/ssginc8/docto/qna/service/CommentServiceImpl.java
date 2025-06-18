package com.ssginc8.docto.qna.service;


import com.ssginc8.docto.global.event.qna.QnaAnsweredEvent;
import com.ssginc8.docto.qna.dto.CommentResponse;
import com.ssginc8.docto.qna.entity.QaComment;
import com.ssginc8.docto.qna.entity.QaPost;
import com.ssginc8.docto.qna.entity.QaStatus;
import com.ssginc8.docto.qna.provider.CommentProvider;
import com.ssginc8.docto.qna.provider.QaPostProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.stream.events.Comment;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentProvider commentProvider;
	private final QaPostProvider qaPostProvider;
	private final ApplicationEventPublisher applicationEventPublisher;

	// 답변 생성
	@Override
	@Transactional
	public CommentResponse createComment(Long qnaPostId, String content) {

		// 1. 게시글 있는지 확인
		var post = qaPostProvider.getById(qnaPostId);

		// 2. 댓글 엔티티 생성·저장
		QaComment comment = QaComment.create(post, content);
		QaComment saved = commentProvider.save(comment);


		// 3. 게시글 상태를 COMPLETED로 갱신
		if (post.getStatus() != QaStatus.COMPLETED) {
			post.setStatus(QaStatus.COMPLETED);
			qaPostProvider.save(post);
		}

		//applicationEventPublisher.publishEvent(new QnaAnsweredEvent(comment));


		return CommentResponse.fromEntity(saved);
	}

	// 답변 리스트 조회
	@Override
	@Transactional(readOnly = true)
	public List<CommentResponse> getCommentsByPost(Long qnaPostId) {

		// 1. 게시글 있는지 확인
		qaPostProvider.getById(qnaPostId);

		// 2. 댓글 리스트 조회
		return commentProvider.findByPostId(qnaPostId).stream()
			.map(CommentResponse::fromEntity)
			.collect(Collectors.toList());
	}


    // 답변 수정
	@Override
	@Transactional
	public CommentResponse updateComment(Long commentId, String content) {
		// 1. 댓글 존재 확인
		QaComment existing = commentProvider.getById(commentId);

		// 2. 본문 수정
		existing.setContent(content);

		QaComment saved = commentProvider.save(existing);
		return CommentResponse.fromEntity(saved);
	}



	// 답변 상세 조회
	@Override
	@Transactional(readOnly = true)
	public CommentResponse getComment(Long commentId) {
		QaComment comment = commentProvider.getById(commentId);
		return CommentResponse.fromEntity(comment);
	}

	// 답변 삭제
	@Override
	@Transactional
	public void deleteComment(Long commentId) {
		// 1. 댓글 조회
		QaComment comment = commentProvider.getById(commentId);

		// 2. QnA 게시글 가져오기
		QaPost qnaPost = comment.getQnaPostId();

		// 3. 댓글 삭제
		commentProvider.deleteById(commentId);

		// 4. QnA 상태를 다시 PENDING으로 복구
		qnaPost.setStatus(QaStatus.PENDING);
	}

}
