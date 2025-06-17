package com.ssginc8.docto.hospital.provider;

import java.nio.channels.FileChannel;
import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.doctor.repo.DoctorRepo;
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
	private final DoctorRepo doctorRepo;
	private final FileRepo fileRepo;






	public Page<Hospital> searchHospitalsWithoutLocation(String query, Pageable pageable) {
		return hospitalRepo.searchHospitalsWithoutLocation(query, pageable);
	}

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

	public HospitalResponse getHospitalByAdminId(Long userId) {
		Hospital hospital = hospitalRepo.findByUserUserId(userId)
			.orElseThrow(HospitalNotFoundException::new);

		String imageUrl = hospital.getFileId() != null
			? fileRepo.getFileUrlById(hospital.getFileId())
			: null;

		List<String> serviceNames = providedServiceRepo.findServiceNamesByHospitalId(hospital.getHospitalId());

		return HospitalResponse.from(hospital, imageUrl, serviceNames);
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

	public void saveServices(List<ProvidedService> services) {
		providedServiceRepo.saveAll(services);
	}

	// HospitalRepo 의존 제거
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
	public void deleteByHospitalHospitalId(Long hospitalId) {
		providedServiceRepo.deleteByHospitalHospitalId(hospitalId);
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
	@Transactional(readOnly = true)
	public Long getHospitalIdByAdminId(Long userId) {
		return hospitalRepo.findByUserUserId(userId)
			.map(Hospital::getHospitalId)
			.orElseThrow(HospitalNotFoundException::new);
	}

	public List<HospitalSchedule> findSchedulesByHospitalIds(List<Long> hospitalIds) {
		return hospitalScheduleRepo.findByHospitalHospitalIdIn(hospitalIds);
	}

	public Map<Long, List<String>> findServiceNamesMapByHospitalIds(List<Long> hospitalIds) {
		List<ProvidedService> list = providedServiceRepo.findByHospitalIds(hospitalIds);
		return list.stream()
			.collect(Collectors.groupingBy(
				p -> p.getHospital().getHospitalId(),
				Collectors.mapping(ProvidedService::getServiceName, Collectors.toList())
			));
	}

	public Map<Long, String> findFileUrlMapByHospitalIds(List<Long> hospitalIds) {
		List<Hospital> hospitals = hospitalRepo.findAllById(hospitalIds);
		return hospitals.stream()
			.filter(h -> h.getFileId() != null)
			.collect(Collectors.toMap(
				Hospital::getHospitalId,
				h -> fileRepo.getFileUrlById(h.getFileId()) // fileId → url
			));
	}

	public String getImageUrl(Long hospitalId) {
		return fileRepo.getFileUrlByHospitalId(hospitalId);
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
}

