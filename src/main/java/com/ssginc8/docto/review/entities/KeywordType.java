package com.ssginc8.docto.review.entities;

import lombok.Getter;

//리뷰 키워드 타입
//label: 실제 보여줄 한글 키워드
//category: 카테고리 구분 (의료서비스(MEDICAL_SERVICE), 시설/환경(FACILITY_ENV), 비용/행정(COST_ADMIN)
//polarity: 긍정/부정 여부
//weight: 가중치


@Getter
public enum KeywordType {
	// 긍정 리뷰 키워드
	//의료서비스
	THOROUGH("진료가 꼼꼼해요", Category.MEDICAL_SERVICE, Polarity.POSITIVE, 5),
	FRIENDLY_DOCTOR("의사가 친절해요", Category.MEDICAL_SERVICE, Polarity.POSITIVE, 5),
	FAST("진료가 빨라요", Category.MEDICAL_SERVICE, Polarity.POSITIVE, 3),
	SHORT_WAIT("대기 시간이 짧아요", Category.MEDICAL_SERVICE, Polarity.POSITIVE, 4),
	PROFESSIONAL("전문성이 느껴져요", Category.MEDICAL_SERVICE, Polarity.POSITIVE, 5),
	SENIOR_FRIENDLY("노인 환자에게 배려가 있어요", Category.MEDICAL_SERVICE, Polarity.POSITIVE, 4),

	//시설,환경
	CLEAN_HOSPITAL("위생이 청결해요", Category.FACILITY_ENV, Polarity.POSITIVE, 5),
	NICE_FACILITY("시설이 좋아요", Category.FACILITY_ENV, Polarity.POSITIVE, 3),
	EASY_PARKING("주차가 편해요", Category.FACILITY_ENV, Polarity.POSITIVE, 4),
	GOOD_LOCATION("위치가 좋아요", Category.FACILITY_ENV, Polarity.POSITIVE, 3),
	COMFORTABLE_ATMOS("분위기가 편안해요", Category.FACILITY_ENV, Polarity.POSITIVE, 4),

	//비용,행정
	FAIR_PRICE("진료비가 합리적이에요", Category.COST_ADMIN, Polarity.POSITIVE, 5),
	EASY_INSURANCE("보험 처리가 편해요", Category.COST_ADMIN, Polarity.POSITIVE, 4),
	FAST_RESULTS("검사 결과가 빨리 나와요", Category.COST_ADMIN, Polarity.POSITIVE, 4),
	ENOUGH_CONSULT("상담 시간이 충분해요", Category.COST_ADMIN, Polarity.POSITIVE, 5),
	WANT_RETURN("재방문하고 싶어요", Category.COST_ADMIN, Polarity.POSITIVE, 5),
	FAST_PAYMENT("수납이 빠르고 편해요", Category.COST_ADMIN, Polarity.POSITIVE, 3),


	// 부정 리뷰 키워드
	//의료서비스
	UNFRIENDLY_EXAM("진료가 불친절해요", Category.MEDICAL_SERVICE, Polarity.NEGATIVE, 5),
	LACK_EXPLANATION("설명이 부족해요", Category.MEDICAL_SERVICE, Polarity.NEGATIVE, 5),
	POOR_COMMUNICATION("환자 말을 잘 안 들어줘요", Category.MEDICAL_SERVICE, Polarity.NEGATIVE, 5),
	NO_EFFECT_TREAT("치료 효과가 없었어요", Category.MEDICAL_SERVICE, Polarity.NEGATIVE, 5),
	LONG_WAIT("대기 시간이 너무 길어요", Category.MEDICAL_SERVICE, Polarity.NEGATIVE, 4),
	WAIT_AFTER_BOOK("예약해도 오래 기다렸어요", Category.MEDICAL_SERVICE, Polarity.NEGATIVE, 4),
	LACK_GUIDE("안내가 부족했어요", Category.MEDICAL_SERVICE, Polarity.NEGATIVE, 3),
	COMPLEX_PAYMENT("접수/수납 과정이 복잡해요", Category.MEDICAL_SERVICE, Polarity.NEGATIVE, 4),

	//시설,환경
	DIRTY_HOSPITAL("병원이 지저분해요", Category.FACILITY_ENV, Polarity.NEGATIVE, 5),
	WORRY_CLEAN("소독/청결이 걱정돼요", Category.FACILITY_ENV, Polarity.NEGATIVE, 5),
	TIGHT_WAIT_AREA("대기실이 좁고 불편해요", Category.FACILITY_ENV, Polarity.NEGATIVE, 4),
	NO_PARKING_SPACE("주차 공간이 부족해요", Category.FACILITY_ENV, Polarity.NEGATIVE, 3),
	CONFUSING_SIGNAGE("안내 표지가 헷갈려요", Category.FACILITY_ENV, Polarity.NEGATIVE, 3),
	NO_WHEELCHAIR_ACCESS("휠체어 접근이 어려워요", Category.FACILITY_ENV, Polarity.NEGATIVE, 4),
	NO_GUARDIAN_SPACE("보호자 공간이 부족해요", Category.FACILITY_ENV, Polarity.NEGATIVE, 3),

	//비용,행정
	EXPENSIVE("진료비가 너무 비싸요", Category.COST_ADMIN, Polarity.NEGATIVE, 5),
	PUSH_UNNECESSARY("불필요한 시술을 권유해요", Category.COST_ADMIN, Polarity.NEGATIVE, 5),
	LACK_FEE_EXPLAN("비용 설명이 부족해요", Category.COST_ADMIN, Polarity.NEGATIVE, 4),
	INSURANCE_BUREAUCRACY("보험 처리가 번거로워요", Category.COST_ADMIN, Polarity.NEGATIVE, 4),
	LATE_RECEIPT("영수증/서류 처리 지연", Category.COST_ADMIN, Polarity.NEGATIVE, 3);

	private final String label;
	private final Category category;
	private final Polarity polarity;
	private final int weight;

	KeywordType(String label, Category category, Polarity polarity, int weight) {
		this.label    = label;
		this.category = category;
		this.polarity = polarity;
		this.weight   = weight;
	}

	public enum Category {
		MEDICAL_SERVICE, FACILITY_ENV, COST_ADMIN
	}

	public enum Polarity {
		POSITIVE, NEGATIVE
	}
}
