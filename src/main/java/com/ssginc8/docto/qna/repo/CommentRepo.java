package com.ssginc8.docto.qna.repo;

import com.ssginc8.docto.qna.entity.QaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepo extends JpaRepository<QaComment, Long> {

	List<QaComment> findByQnaPostIdQnaPostId(Long qnaPostId);
}
