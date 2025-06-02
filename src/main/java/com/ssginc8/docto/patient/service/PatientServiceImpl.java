package com.ssginc8.docto.patient.service;

import com.ssginc8.docto.global.error.exception.patientException.RRNEncryptionFailedException;
import com.ssginc8.docto.patient.dto.PatientRequest;
import com.ssginc8.docto.patient.dto.PatientResponse;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.provider.PatientProvider;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

	private final PatientProvider patientProvider;
	private final UserProvider userProvider;

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
			.map(PatientResponse::from);
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
