package com.ssginc8.docto.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.dto.AppointmentResponseDto;
import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.appointment.provider.SymptomProvider;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.qna.provider.QaPostProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentProvider appointmentProvider;

	private final SymptomProvider symptomProvider;
	private final QaPostProvider qaPostProvider;

	@Transactional(readOnly = true)
	@Override
	public AppointmentResponseDto getAppointmentDetail(Long appointmentId) {
		Appointment appointment = appointmentProvider.getAppointmentById(appointmentId);

		Hospital hospital = appointment.getHospital();
		Doctor doctor = appointment.getDoctor();
		PatientGuardian guardian = appointment.getPatientGuardian();

		List<String> symptoms = symptomProvider.getSymptomsByAppointment(appointment);
		String qaPost = qaPostProvider.getQaPostByAppointment(appointment);

		return AppointmentResponseDto.builder()
			.appointmentId(appointment.getAppointmentId())
			.hospitalId(hospital.getHospitalId())
			.doctorId(doctor.getDoctorId())
			.patientGuardianId(guardian.getPatientGuardianId())
			.hospitalName(hospital.getName())
			.doctorName(doctor.getUser().getName()) // 의사 이름 구하는 로직
			.patientName(guardian.getPatient().getUser().getName()) // 환자 이름 구하는 로직
			.symptoms(symptoms)
			.question(qaPost)
			.appointmentTime(appointment.getAppointmentTime())
			.appointmentType(appointment.getAppointmentType())
			.paymentType(appointment.getPaymentType())
			.status(appointment.getStatus())
			.build();
	}
}
