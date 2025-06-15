package com.ssginc8.docto.appointment.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ssginc8.docto.appointment.dto.TimeSlotDto;
import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.DoctorSchedule;

public class TimeSlotUtil {

	/**
	 * 의사의 스케줄을 기반으로 30분 단위의 예약 가능한 시간 슬롯들을 반환합니다.
	 *
	 * @param schedule 의사의 하루 스케줄 (근무 시작/종료, 점심시간 포함)
	 * @param baseDate 기준 날짜 (예: 2025-06-01)
	 * @return 예약 가능한 시간 슬롯 목록 (시작 시간과 끝 시간이 담긴 배열)
	 */
	public static List<TimeSlotDto> getTimeSlotsWithAvailability(
		DoctorSchedule schedule,
		Doctor doctor,
		LocalDateTime baseDate,
		AppointmentProvider appointmentProvider
	) {
		List<TimeSlotDto> slots = new ArrayList<>();

		LocalDateTime start = baseDate.with(schedule.getStartTime());
		LocalDateTime end = baseDate.with(schedule.getEndTime());
		LocalDateTime lunchStart = baseDate.with(schedule.getLunchStart());
		LocalDateTime lunchEnd = baseDate.with(schedule.getLunchEnd());

		Long maxCapacity = doctor.getCapacityPerHalfHour();

		while (start.isBefore(end)) {
			LocalDateTime slotEnd = start.plusMinutes(30);

			if (slotEnd.isAfter(lunchStart) && start.isBefore(lunchEnd)) {
				start = lunchEnd;
				continue;
			}

			if (slotEnd.isAfter(end)) break;

			boolean available = true;

			// 현재 시간보다 과거인지 확인
			if (start.isBefore(LocalDateTime.now())) {
				available = false;
			}

			// 예약 수 초과 여부 확인
			if (available && maxCapacity != null && maxCapacity > 0) {
				int currentAppointments =
					appointmentProvider.countAppointmentsInSlot(doctor.getDoctorId(), start, slotEnd);
				if (currentAppointments >= maxCapacity) {
					available = false;
				}
			}

			slots.add(new TimeSlotDto(start, slotEnd, available));
			start = slotEnd;
		}

		return slots;
	}
}
