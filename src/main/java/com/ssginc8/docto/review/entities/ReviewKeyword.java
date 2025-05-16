package com.ssginc8.docto.review.entities;


import com.ssginc8.docto.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_review_keyword")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewKeyword extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reviewKeywordId")
	private Long reviewKeywordId;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewId", nullable = false)
	private Review review;


	@Enumerated(EnumType.STRING)
	@Column(name = "keyword", nullable = false, length = 50)
	private KeywordType keyword;


	protected ReviewKeyword(Review review, KeywordType keyword) {
		this.review  = review;
		this.keyword = keyword;
	}


	public static ReviewKeyword of(Review review, KeywordType keyword) {
		ReviewKeyword rk = new ReviewKeyword(review, keyword);
		review.getKeywords().add(rk);
		return rk;
	}

	// //키워드 수정용 메서드 필요할까요
	// public void updateKeyword(KeywordType newKeyword) {
	// 	this.keyword = newKeyword;
	// }
}
