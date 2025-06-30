package com.ssginc8.docto.medication.entity;

import com.ssginc8.docto.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 복약 시간 Entity
 *
 * - 복약 정보(MedicationInformation)에 연관된 복약 시간
 * - 복약 요일(MedicationAlertDay) 및 복약 기록(MedicationLog)과 연결됨
 */
@Entity
@Table(name = "tbl_medication_alert_time")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicationAlertTime extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long medicationAlertTimeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medication_id", nullable = false)
	private MedicationInformation medication;

	@Column(nullable = false, length = 10)
	private String meal;           // "morning", "lunch", "dinner" 중 하나

	@Column(name = "time_to_take", nullable = false)
	private LocalTime timeToTake;

	@OneToMany(
		mappedBy = "medicationAlertTime",
		cascade = CascadeType.ALL,
		orphanRemoval = true,
		fetch = FetchType.LAZY
	)
	private List<MedicationAlertDay> alertDays = new ArrayList<>();

	/**
	 * 팩토리 메서드
	 * @param medication 부모 MedicationInformation
	 * @param meal      끼니 구분 ("morning" | "lunch" | "dinner")
	 * @param timeToTake 복용 시간
	 */
	public static MedicationAlertTime create(
		MedicationInformation medication,
		String meal,
		LocalTime timeToTake
	) {
		MedicationAlertTime at = new MedicationAlertTime();
		at.medication = medication;
		at.meal       = meal;
		at.timeToTake = timeToTake;
		return at;
	}

	/**
	 * 시간만 수정할 경우 사용
	 */
	public void updateTimeToTake(LocalTime newTime) {
		this.timeToTake = newTime;
	}
}
