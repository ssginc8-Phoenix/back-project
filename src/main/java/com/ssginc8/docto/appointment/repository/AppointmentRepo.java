package com.ssginc8.docto.appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssginc8.docto.appointment.entity.Appointment;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
	// 필요하다면 커스텀 조회 메서드를 추가할 수도 있습니다.
}
