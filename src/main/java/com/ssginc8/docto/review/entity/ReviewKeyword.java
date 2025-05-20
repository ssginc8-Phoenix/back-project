package com.ssginc8.docto.review.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_review_keyword")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewKeyword {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reviewKeywordId")
	private Long reviewKeywordId;


	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "reviewId", nullable = false)
	private Review review;


	@Enumerated(EnumType.STRING)
	@Column(name = "keywords", nullable = false, length = 50)
	private KeywordType keywords;


	private ReviewKeyword(Review review, KeywordType keywords) {
		validate(review, keywords);
		this.review = review;
		this.keywords = keywords;
	}

	public static ReviewKeyword of(Review review, KeywordType keywords) {
		return new ReviewKeyword(review, keywords);
	}

	//유효성 검사
	private static void validate(Review review, KeywordType keywords) {
		if (review == null) {
			throw new IllegalArgumentException("review는 null일 수 없습니다.");
		}
		if (keywords == null) {
			throw new IllegalArgumentException("keywords는 null일 수 없습니다.");
		}
	}




}
