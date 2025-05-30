package com.ssginc8.docto.qna.entity;



import com.ssginc8.docto.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_qna_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QaComment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qna_comment_id")
	private Long qnaCommentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qna_post_id", nullable = false)
	private QaPost qnaPostId;

	@Setter
	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	public static QaComment create(QaPost post, String content) {
		QaComment comment = new QaComment();
		comment.qnaPostId = post;
		comment.content = content;
		return comment;
	}



}
