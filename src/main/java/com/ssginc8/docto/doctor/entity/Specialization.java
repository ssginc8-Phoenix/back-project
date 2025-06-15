package com.ssginc8.docto.doctor.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Specialization {

	CARDIOLOGY("심장내과"),
	NEUROLOGY("신경과"),
	DERMATOLOGY("피부과"),
	PEDIATRICS("소아과"),
	RADIOLOGY("영상의학과"),
	ONCOLOGY("종양내과"),
	GYNECOLOGY("산부인과"),
	PSYCHIATRY("정신과"),
	GENERAL_SURGERY("일반외과"),
	UROLOGY("비뇨기과"),
	OPHTHALMOLOGY("안과"),
	ENT("이비인후과"),
	INTERNAL_MEDICINE("내과");

	private final String description;
}
