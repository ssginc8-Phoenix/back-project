package com.ssginc8.docto.qna.repo;

import com.ssginc8.docto.qna.entity.QaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepo extends JpaRepository<QaComment, Long> {

	List<QaComment> findByQnaPostIdQnaPostId(Long qnaPostId);

	@Query("""
		SELECT u, h.name, pg.patient.user.name, a.appointmentTime
		FROM QaComment qc
		JOIN qc.qnaPostId qp
		JOIN qp.appointment a
		JOIN a.hospital h
		JOIN a.patientGuardian pg
		JOIN pg.user u
		WHERE qc.qnaCommentId = :qnaCommentId
	""")
	Object[] findNotificationDataByQnaCommentId(@Param("qnaCommentId") Long qnaCommentId);
}
