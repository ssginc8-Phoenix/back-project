package com.ssginc8.docto.guardian.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.global.error.exception.guardianException.InvalidInviteCodeException;
import com.ssginc8.docto.global.error.exception.guardianException.InvalidGuardianStatusException;
import com.ssginc8.docto.guardian.dto.GuardianInviteResponse;
import com.ssginc8.docto.guardian.dto.PatientSummaryResponse;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.entity.Status;
import com.ssginc8.docto.guardian.provider.GuardianProvider;
import com.ssginc8.docto.guardian.provider.PatientGuardianProvider;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.provider.PatientProvider;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GuardianServiceImpl implements GuardianService {

	private final GuardianProvider guardianProvider;
	private final PatientGuardianProvider patientGuardianProvider;
	private final PatientProvider patientProvider;
	private final UserProvider userProvider;

	@Override
	public GuardianInviteResponse inviteGuardian(Long patientId, String guardianEmail) {
		Patient patient = patientProvider.getActivePatient(patientId);
		User guardian = userProvider.loadUserByEmailOrException(guardianEmail);

		String inviteCode = generateInviteCode(patient.getPatientId(), guardian.getUserId());

		PatientGuardian pg = PatientGuardian.create(guardian, patient, LocalDateTime.now());
		pg.updateInviteCode(inviteCode);

		patientGuardianProvider.save(pg);

		return new GuardianInviteResponse(inviteCode);
	}

	@Override
	public void updateStatus(Long requestId, String inviteCode, String statusStr) {
		PatientGuardian pg = guardianProvider.getById(requestId);

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
		PatientGuardian pg = guardianProvider.getMapping(guardianId, patientId);
		pg.delete();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientSummaryResponse> getAllAcceptedMappings(Long guardianId) {
		return guardianProvider.getAllAcceptedMappings(guardianId).stream()
			.map(pg -> PatientSummaryResponse.of(
				pg.getPatient().getPatientId(),
				pg.getPatient().getUser().getName(),
				pg.getPatient().getResidentRegistrationNumber()
			))
			.collect(Collectors.toList());
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
}
