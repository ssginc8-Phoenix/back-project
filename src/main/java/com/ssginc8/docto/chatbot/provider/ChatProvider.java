package com.ssginc8.docto.chatbot.provider;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.doctor.entity.Specialization;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.repo.HospitalRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatProvider {

	private final HospitalRepo hospitalRepo;

	public List<Hospital> fetchHospitals(Specialization spec) {
		return hospitalRepo.findTop5BySpecializationOrderByWaitingAsc(spec);
	}
}

