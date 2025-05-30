package com.ssginc8.docto.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ssginc8.docto.doctor.entity.DoctorSchedule;

public class TimeSlotUtil {

	/**
	 * 의사의 스케줄을 기반으로 30분 단위의 예약 가능한 시간 슬롯들을 반환합니다.
	 *
	 * @param schedule 의사의 하루 스케줄 (근무 시작/종료, 점심시간 포함)
	 * @param baseDate 기준 날짜 (예: 2025-06-01)
	 * @return 예약 가능한 시간 슬롯 목록 (시작 시간과 끝 시간이 담긴 배열)
	 */
	public static List<LocalDateTime[]> getAvailableTimeSlots(DoctorSchedule schedule, LocalDateTime baseDate) {
		List<LocalDateTime[]> slots = new ArrayList<>();

		// 스케줄의 시간들을 기준 날짜에 결합 (날짜는 동일하지만 시간만 다름)
		LocalDateTime start = baseDate.with(schedule.getStartTime());
		LocalDateTime end = baseDate.with(schedule.getEndTime());
		LocalDateTime lunchStart = baseDate.with(schedule.getLunchStart());
		LocalDateTime lunchEnd = baseDate.with(schedule.getLunchEnd());

		// 근무 시간 동안 반복하면서 30분 단위 슬롯 생성
		while (start.isBefore(end)) {
			LocalDateTime slotEnd = start.plusMinutes(30);	// 30분 뒤 시간 계산

			// 현재 슬롯이 점심시간과 겹치는 경우 -> 점심시간 이후로 이동
			if (slotEnd.isAfter(lunchStart) && start.isBefore(lunchEnd)) {
				start = lunchEnd;	// 점심 끝나는 시간으로 건너뜀
				continue;
			}
			
			// 현재 슬롯의 끝 시간이 근무 종료 시간보다 늦으면 루프 종료
			if (slotEnd.isAfter(end)) break;

			// 점심시간이 아니고, 유효한 시간대이면 슬롯 추가
			slots.add(new LocalDateTime[] { start, slotEnd });
			
			// 다음 슬롯으로 이동
			start = slotEnd;
		}

		return slots;
	}
}
