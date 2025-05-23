package com.ssginc8.docto.qna.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.qna.entity.QaPost;

public interface QaPostRepo extends JpaRepository<QaPost, Long> {

	Optional<QaPost> findByAppointment(Appointment appointment);
}
