package com.ssginc8.docto.qna.provider;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.qna.entity.QaComment;
import com.ssginc8.docto.qna.repo.CommentRepo;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentProvider {

	private final CommentRepo commentRepo;


	// Id로 댓글 조회
	@Transactional(readOnly = true)
	public QaComment getById(Long commentId) {
		return commentRepo.findById(commentId)
			.orElseThrow(() ->
				new IllegalArgumentException("댓글이 없습니다. id=" + commentId)
			);
	}


	// 게시글에 달린 댓글 조회
	@Transactional(readOnly = true)
	public List<QaComment> findByPostId(Long qnaPostId) {
		return commentRepo.findByQnaPostIdQnaPostId(qnaPostId);
	}


	// 댓글 저장
	@Transactional
	public QaComment save(QaComment comment) {
		return commentRepo.save(comment);
	}

	// 댓글 삭제
	@Transactional
	public void deleteById(Long commentId) {
		commentRepo.deleteById(commentId);
	}
}
