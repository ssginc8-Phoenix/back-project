package com.ssginc8.docto.hospital.dto;



import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssginc8.docto.file.entity.File;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data

@AllArgsConstructor
@Builder
@Getter
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

	@NotNull(message = "서비스이름은 필수입니다.")
	private List<String> serviceName;

	@NotNull(message = "사업자번호는 필수입니다.")
	private String businessRegistrationNumber;

	private BigDecimal latitude;

	private BigDecimal longitude;

	private List<MultipartFile> files;

	private List<Long> existingFileIds;



}
