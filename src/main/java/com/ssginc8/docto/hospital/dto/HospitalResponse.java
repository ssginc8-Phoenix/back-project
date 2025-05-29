package com.ssginc8.docto.hospital.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data

@NoArgsConstructor
@Builder
@Getter
@AllArgsConstructor
public class HospitalResponse {

	private Long hospitalId;

	private Long userId;

	private String name;

	private String address;

	private BigDecimal latitude;

	private BigDecimal longitude;

	private String phone;

	private String introduction;

	private String notice;

	private Long waiting;

	private String businessRegistrationNumber;

	private List<String> serviceNames;


}
