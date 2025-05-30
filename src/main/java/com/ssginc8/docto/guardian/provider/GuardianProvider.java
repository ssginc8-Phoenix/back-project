package com.ssginc8.docto.guardian.provider;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.repo.PatientGuardianRepo;

import lombok.RequiredArgsConstructor;

/**
 * GuardianProvider는 엔티티 조회 전용 컴포넌트입니다.
 * - Controller나 Service에서 직접 repository를 호출하지 않도록 도와줍니다.
 * - 중복된 예외 처리 코드를 줄이고, 엔티티 조회 책임을 분리합니다.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuardianProvider {

	private final PatientGuardianRepo patientGuardianRepository;

	/**
	 * ID 기반으로 환자-보호자 관계 엔티티를 조회합니다.
	 * @param requestId 보호자 요청 ID
	 * @return 환자-보호자 엔티티
	 * @throws NoSuchElementException 존재하지 않을 경우 예외 발생
	 */
	public PatientGuardian getById(Long requestId) {
		return patientGuardianRepository.findById(requestId)
			.orElseThrow(() -> new NoSuchElementException("해당 요청이 존재하지 않습니다. id=" + requestId));
	}
}