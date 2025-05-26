package com.ssginc8.docto.hospital.dto;



import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data

@AllArgsConstructor
@Builder
//병원 등록/수정
public class HospitalRequest {

	private Long userId;

	@NotNull(message = "병원 이름은 필수입니다.")
	private String name;

	@NotNull(message = "병원 주소는 필수입니다.")
	private String address;

	@NotNull(message = "병원 전화번호는 필수입니다.")
	private String phone;

	private String introduction;

	private String notice;

	@NotNull(message = "사업자등록번호는 필수입니다.")
	private String businessRegistrationNumber;

	@NotNull(message = "서비스이름은 필수입니다.")
	private List<String> serviceName;



	private BigDecimal latitude;

	private BigDecimal longitude;



}
