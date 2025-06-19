package com.ssginc8.docto.hospital.provider;

import java.nio.channels.FileChannel;
import java.time.DayOfWeek;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.doctor.repo.DoctorRepo;
import com.ssginc8.docto.file.provider.FileProvider;
import com.ssginc8.docto.file.repository.FileRepo;
import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;
import com.ssginc8.docto.global.error.exception.hospitalException.HospitalNotFoundException;
import com.ssginc8.docto.global.error.exception.hospitalException.ScheduleNotFoundByDayException;
import com.ssginc8.docto.global.error.exception.hospitalException.ScheduleNotFoundException;
import com.ssginc8.docto.global.error.exception.hospitalException.ScheduleNotInHospitalException;
import com.ssginc8.docto.global.error.exception.userException.UserNotFoundException;
import com.ssginc8.docto.hospital.dto.HospitalResponse;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.entity.HospitalSchedule;
import com.ssginc8.docto.hospital.entity.ProvidedService;
import com.ssginc8.docto.hospital.repo.HospitalRepo;
import com.ssginc8.docto.hospital.repo.HospitalScheduleRepo;
import com.ssginc8.docto.hospital.repo.ProvidedServiceRepo;

import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.repo.UserRepo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HospitalProvider {

	private final HospitalRepo hospitalRepo;
	private final UserRepo userRepo;
	private final HospitalScheduleRepo hospitalScheduleRepo;
	private final ProvidedServiceRepo providedServiceRepo;
	private final FileRepo fileRepo;
	private final FileProvider fileProvider;








	@Transactional(readOnly = true)
	public Hospital getHospitalById(Long hospitalId) {
		return hospitalRepo.findByHospitalIdAndDeletedAtIsNull(hospitalId)
			.orElseThrow(HospitalNotFoundException::new);
	}

	public User getUserById(Long userId) {
		return userRepo.findById(userId)
			.orElseThrow(UserNotFoundException::new);
	}

	public HospitalSchedule getScheduleByIdOrThrow(Long scheduleId) {
		return hospitalScheduleRepo.findById(scheduleId)
			.orElseThrow(ScheduleNotFoundException::new);
	}





	public void validateScheduleBelongsToHospital(HospitalSchedule schedule, Hospital hospital) {
		if (!schedule.getHospital().getHospitalId().equals(hospital.getHospitalId())) {
			throw new ScheduleNotInHospitalException();
		}
	}

	public void saveHospital(Hospital hospital) {
		hospitalRepo.save(hospital);
	}

	public HospitalSchedule saveHospitalSchedule(HospitalSchedule schedule) {
		hospitalScheduleRepo.save(schedule);
		return schedule;
	}




	public Page<Hospital> findHospitalsWithinRadius(double lat, double lng, double radius, String query, Pageable pageable) {
		return hospitalRepo.findHospitalsWithinRadius(lat, lng, radius,query,  pageable);
	}



	@Transactional
	public void deleteByHospitalScheduleId(Long hospitalScheduleId) {
		HospitalSchedule schedule = hospitalScheduleRepo.findById(hospitalScheduleId)
			.orElseThrow(ScheduleNotFoundException::new);
		hospitalScheduleRepo.delete(schedule);
	}


	public List<ProvidedService> findServicesByHospitalId(Long hospitalId) {
		return providedServiceRepo.findByHospitalHospitalId(hospitalId);
	}


	public void deleteProvidedServicesByHospital(Hospital hospital) {
		providedServiceRepo.deleteAllByHospital(hospital);
	}




	public Page<Hospital> findAll(Pageable pageable) {
		return hospitalRepo.findAllByDeletedAtIsNull(pageable);
	}

	public List<HospitalSchedule> findSchedulesByHospitalId(Long hospitalId) {
		return hospitalScheduleRepo.findByHospitalHospitalId(hospitalId);
	}


	public HospitalSchedule getScheduleByDay(Long hospitalId, DayOfWeek dayOfWeek) {
		return hospitalScheduleRepo.findByHospitalHospitalIdAndDayOfWeek(hospitalId, dayOfWeek)
			.orElseThrow(ScheduleNotFoundByDayException::new);
	}

	public void deleteByHospitalId(Long hospitalId) {
		hospitalScheduleRepo.deleteAllByHospitalHospitalId(hospitalId);
	}

	@Transactional(readOnly = true)
	public Hospital findByUserUserId(Long userId) {
		return hospitalRepo.findByUserUserId(userId)
			.orElseThrow(HospitalNotFoundException::new);
	}






	public String getImageUrl(Long hospitalId) {
		return fileRepo.getFileUrlById((hospitalId));
	}

	public List<String> getServiceNames(Long hospitalId) {
		return providedServiceRepo.findServiceNamesByHospitalId(hospitalId);
	}

	public Page<Hospital> findAllNearby(
		String query,
		double latitude,
		double longitude,
		double radius,
		Pageable pageable
	) {
		// repository 의 커스텀 메서드 호출
		return hospitalRepo.findAllNearby(query, latitude, longitude, radius, pageable);
	}

	/**
	 * 일반 검색
	 */
	public Page<Hospital> findAll(
		Specification<Hospital> spec,
		Pageable pageable
	) {
		return hospitalRepo.findAll(spec, pageable);
	}

	public void saveAll(List<ProvidedService> svs) {
		providedServiceRepo.saveAll(svs);
	}

	public List<String> findServiceNamesByHospitalId(Long hospitalId) {
		return providedServiceRepo.findServiceNamesByHospitalId(hospitalId);
	}





	@Transactional
	public void deleteServiceById(Long serviceId) {
		providedServiceRepo.deleteById(serviceId);
	}


	@Transactional
	public ProvidedService saveService(ProvidedService service) {
		return providedServiceRepo.save(service);
	}
}

