package com.ssginc8.docto.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ssginc8.docto.review.entity.KeywordType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class ReviewUpdateRequest{


	private LocalDateTime updatedAt;

	@NotBlank(message = "contents는 빈 값일 수 없습니다.")
	@Size(max = 1000, message = "contents는 최대 1000자까지 입력 가능합니다.")
	private String contents;

	@Size(min = 3, max = 8, message = "키워드는 3~8개 사이로 선택 가능합니다.")
	private List<KeywordType> keywords;


}
