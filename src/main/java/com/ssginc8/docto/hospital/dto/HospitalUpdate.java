package com.ssginc8.docto.hospital.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Data

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class HospitalUpdate {


	private String name;
	private String address;
	private String phone;
	private String introduction;
	private String notice;
	private List<String> serviceNames;
	private String businessRegistrationNumber;
	private List<Long> existingFileIds;
	private List<MultipartFile> files;
	private List<Long> deletedFileIds;



}
