package com.ssginc8.docto.review.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import com.ssginc8.docto.review.dto.ReviewSummaryResponse;
import com.ssginc8.docto.review.provider.ReviewSummaryProvider;

@Service
public class ReviewSummaryServiceImpl implements ReviewSummaryService {

	private final ReviewSummaryProvider provider;
	private final ChatClient chatClient;

	public ReviewSummaryServiceImpl(
		ReviewSummaryProvider provider,
		ChatClient chatClient) {
		this.provider   = provider;
		this.chatClient = chatClient;
	}

	@Override
	public ReviewSummaryResponse summarize(Long hospitalId) {

		List<String> reviews = provider.getContents(hospitalId);
		if (reviews.isEmpty()) {
			return new ReviewSummaryResponse(hospitalId, 0, "아직 리뷰가 없습니다.");
		}

		String prompt = """
            다음은 병원 리뷰 모음입니다.
            리뷰를 모두 읽어보고 장점만 두 문장으로 요약해주세요.
            ---
            %s
            """.formatted(String.join("", reviews.stream().limit(30).toList()));

		String summary = chatClient
			.prompt(prompt)
			.call()
			.content()
			.trim();


		return new ReviewSummaryResponse(hospitalId, reviews.size(), summary);
	}
}
