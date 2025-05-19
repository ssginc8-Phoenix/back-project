package com.ssginc8.docto.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HospitalWaitingDTO {

	private Long HospitalId;

	private Long waiting;
}
