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


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewId", nullable = false)
	private Review review;


	@Enumerated(EnumType.STRING)
	@Column(name = "keyword", nullable = false, length = 50)
	private KeywordType keyword;


	@Builder
	private ReviewKeyword(Review review, KeywordType keyword) {
		this.review = review;
		this.keyword = keyword;


	}

}
