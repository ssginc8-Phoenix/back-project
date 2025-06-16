package com.ssginc8.docto.patient.service;

import com.ssginc8.docto.file.provider.FileProvider;
import com.ssginc8.docto.patient.dto.PatientRequest;
import com.ssginc8.docto.patient.dto.PatientResponse;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.provider.PatientProvider;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ssginc8.docto.global.error.exception.patientException.RRNEncryptionFailedException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

	private final PatientProvider patientProvider;
	private final UserProvider userProvider;
	private final FileProvider fileProvider;

	@Value("${cloud.default.image.address}")
	private String defaultProfileUrl;

	@Override
	@Transactional
	public Long createPatient(PatientRequest dto) {
		User user = userProvider.getUserById(dto.getUserId());
		String encryptedRRN = encryptRRN(dto.getResidentRegistrationNumber());
		Patient patient = Patient.create(user, encryptedRRN);
		Patient savedPatient = patientProvider.savePatient(patient);
		return savedPatient.getPatientId();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<PatientResponse> getAllPatients(Pageable pageable) {
		return patientProvider.getAllActivePatients(pageable)
			.map(patient -> {
				PatientResponse dto = PatientResponse.from(patient);

				// 1) User 엔티티에서 FileId 꺼내오기
				Long fileId = patient.getUser().getProfileImage() != null
					? patient.getUser().getProfileImage().getFileId()
					: null;

				// 2) S3 URL 조회 (null 이면 default)
				String url = fileProvider.getFileUrlById(fileId);
				dto.setProfileImageUrl(
					(url != null && !url.isBlank()) ? url : defaultProfileUrl
				);

				return dto;
			});
	}

	@Override
	@Transactional(readOnly = true)
	public PatientResponse getPatientByUserId(Long userId) {
		Patient patient = patientProvider.getPatientByUserId(userId);
		return PatientResponse.from(patient);
	}

	@Override
	@Transactional
	public void deletePatient(Long patientId) {
		Patient patient = patientProvider.getActivePatient(patientId);
		patient.delete(); // BaseTimeEntity의 soft delete
	}

	private String encryptRRN(String rrn) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(rrn.getBytes());
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				hexString.append(String.format("%02x", b));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RRNEncryptionFailedException();
		}
	}
}
