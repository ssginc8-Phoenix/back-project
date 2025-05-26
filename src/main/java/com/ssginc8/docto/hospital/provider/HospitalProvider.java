package com.ssginc8.docto.hospital.provider;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.entity.HospitalSchedule;
import com.ssginc8.docto.hospital.entity.ProvidedService;
import com.ssginc8.docto.hospital.repository.HospitalRepo;
import com.ssginc8.docto.hospital.repository.HospitalScheduleRepo;
import com.ssginc8.docto.hospital.repository.ProvidedServiceRepo;
import com.ssginc8.docto.hospital.repository.UserRepository;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HospitalProvider {

	private final HospitalRepo hospitalRepo;
	private final UserRepository userRepository;
	private final HospitalScheduleRepo hospitalScheduleRepo;
	private final ProvidedServiceRepo providedServiceRepo;


	@Transactional(readOnly = true)
	public Hospital getHospitalById(Long hospitalId) {
		return hospitalRepo.findById(hospitalId)
			.orElseThrow(() -> new EntityNotFoundException("Hospital not found with id: " + hospitalId));
	}

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
	}

	public HospitalSchedule getScheduleByIdOrThrow(Long scheduleId) {
		return hospitalScheduleRepo.findById(scheduleId)
			.orElseThrow(() -> new EntityNotFoundException("스케줄을 찾을 수 없습니다: " + scheduleId));
	}

	public void validateScheduleBelongsToHospital(HospitalSchedule schedule, Hospital hospital) {
		if (!schedule.getHospital().getHospitalId().equals(hospital.getHospitalId())) {
			throw new IllegalArgumentException("해당 스케줄은 병원에 속하지 않습니다.");
		}
	}
		return hospitalRepo.findById(hospitalId).orElseThrow(
			() -> new IllegalArgumentException("해당 병원이 존재하지 않습니다. id = " + hospitalId)
		);
	}
}

	public void saveHospital(Hospital hospital) {
		hospitalRepo.save(hospital);
	}

	public HospitalSchedule saveHospitalSchedule(HospitalSchedule schedule) {
		hospitalScheduleRepo.save(schedule);
		return schedule;
	}

	public void saveServices(List<ProvidedService> services) {
		providedServiceRepo.saveAll(services);
	}

}
