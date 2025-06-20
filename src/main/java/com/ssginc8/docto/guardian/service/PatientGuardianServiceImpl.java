package com.ssginc8.docto.guardian.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.file.provider.FileProvider;
import com.ssginc8.docto.global.error.exception.guardianException.GuardianAlreadyExistsException;
import com.ssginc8.docto.global.error.exception.guardianException.InvalidInviteCodeException;
import com.ssginc8.docto.global.error.exception.guardianException.InvalidGuardianStatusException;
import com.ssginc8.docto.global.event.EmailSendEvent;
import com.ssginc8.docto.global.event.guardian.GuardianInviteEvent;
import com.ssginc8.docto.global.util.AESUtil;
import com.ssginc8.docto.guardian.dto.GuardianInviteResponse;
import com.ssginc8.docto.guardian.dto.GuardianResponse;
import com.ssginc8.docto.guardian.dto.PatientSummaryResponse;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.entity.Status;
import com.ssginc8.docto.guardian.provider.PatientGuardianProvider;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.provider.PatientProvider;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientGuardianServiceImpl implements PatientGuardianService {

	private final PatientGuardianProvider patientGuardianProvider;
	private final PatientProvider patientProvider;
	private final UserProvider userProvider;
	private final ApplicationEventPublisher eventPublisher;
	private final FileProvider fileProvider;

	@Value("${cloud.default.image.address}")
	private String defaultProfileUrl;

	@Override
	public GuardianInviteResponse inviteGuardian(Long patientId, String guardianEmail) {
		Patient patient = patientProvider.getActivePatient(patientId);
		User patientUser = patient.getUser();
		User guardian = userProvider.loadUserByEmailOrException(guardianEmail);

		Optional<PatientGuardian> existing = patientGuardianProvider.findPendingOrAcceptedMapping(guardian, patient);

		String inviteCode;
		if (existing.isPresent()) {
			PatientGuardian pg = existing.get();
			if (pg.getStatus() == Status.PENDING) {
				// 이미 PENDING이면 초대코드만 갱신
				inviteCode = generateInviteCode(patient.getPatientId(), guardian.getUserId());
				pg.updateInviteCode(inviteCode);
			} else {
				// ACCEPTED인 보호자는 다시 초대 못함
				throw new GuardianAlreadyExistsException();
			}
		} else {
			// 없으면 새로 초대
			inviteCode = generateInviteCode(patient.getPatientId(), guardian.getUserId());
			PatientGuardian newPg = PatientGuardian.create(guardian, patient, LocalDateTime.now());
			newPg.updateInviteCode(inviteCode);
			patientGuardianProvider.save(newPg);

			// 알림 전송받는 USER receiver = 보호자,
			eventPublisher.publishEvent(new GuardianInviteEvent(guardian, patientUser.getName(),
				newPg.getPatientGuardianId()));
		}

		// 🔥 이메일 발송 이벤트 추가
		eventPublisher.publishEvent(EmailSendEvent.guardianInvite(guardianEmail, inviteCode));

		return new GuardianInviteResponse(inviteCode);
	}

	@Override
	public void updateStatusByInviteCode(String inviteCode, String statusStr) {
		PatientGuardian pg = patientGuardianProvider.findByInviteCode(inviteCode);

		Status newStatus;
		try {
			newStatus = Status.valueOf(statusStr);
		} catch (IllegalArgumentException e) {
			throw new InvalidGuardianStatusException();
		}

		pg.updateStatus(newStatus);
	}

	@Override
	public void updateStatus(Long requestId, String inviteCode, String statusStr) {
		PatientGuardian pg = patientGuardianProvider.getById(requestId);

		if (!pg.getInviteCode().equals(inviteCode)) {
			throw new InvalidInviteCodeException();
		}

		Status newStatus;
		try {
			newStatus = Status.valueOf(statusStr);
		} catch (IllegalArgumentException e) {
			throw new InvalidGuardianStatusException();
		}

		pg.updateStatus(newStatus);
	}

	@Override
	public void deleteMapping(Long guardianId, Long patientId) {
		// Provider 를 통해 soft‑delete 호출
		patientGuardianProvider.deleteMapping(guardianId, patientId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientSummaryResponse> getAllAcceptedMappings(Long guardianId) {
		return patientGuardianProvider.getAllAcceptedMappings(guardianId).stream()
			.map(pg -> {
				Patient patient = pg.getPatient();
				User user = patient.getUser();
				// 1) DTO 기본 정보 세팅
				PatientSummaryResponse dto = PatientSummaryResponse.of(
					patient.getPatientId(),
					user.getName(),
					decryptRRN(patient.getResidentRegistrationNumber())
				);
				// 2) 프로필 이미지 URL 가져오기 (FileProvider)
				Long fileId = user.getProfileImage() != null
					? user.getProfileImage().getFileId()
					: null;
				String url = fileProvider.getFileUrlById(fileId);
				// 3) URL 이 없으면 default
				dto.setProfileImageUrl(
					(url != null && !url.isBlank())
						? url
						: defaultProfileUrl
				);
				return dto;
			})
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<GuardianResponse> getGuardiansByPatientId(Long patientId) {
		return patientGuardianProvider.getAllAcceptedGuardiansByPatientId(patientId).stream()
			.map(pg -> GuardianResponse.from(pg.getPatientGuardianId(), pg.getUser().getName()))
			.collect(Collectors.toList());
	}

	@Override
	public void deleteMappingByMappingId(Long mappingId) {
		PatientGuardian pg = patientGuardianProvider.getById(mappingId);
		pg.delete();  // BaseTimeEntity 의 delete() 호출 (soft delete)
	}

	private String generateInviteCode(Long patientId, Long userId) {
		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String rawString = patientId + "-" + userId + "-" + date;
		return "inv-" + patientId + "-" + userId + "-" + date + "-" + sha256(rawString).substring(0, 6);
	}

	private String sha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				hexString.append(String.format("%02x", b));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("해시 생성 중 오류 발생", e);
		}
	}

	private String decryptRRN(String encryptedRRN) {
		return AESUtil.decrypt(encryptedRRN);
	}
}
