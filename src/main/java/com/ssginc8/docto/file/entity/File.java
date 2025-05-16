package com.ssginc8.docto.file.entity;

import org.hibernate.annotations.DynamicUpdate;

import com.ssginc8.docto.global.base.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table
@Entity
public class File extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fileId;

	@Enumerated(EnumType.STRING)
	private Category category;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private String originalName;

	@Column(nullable = false)
	private String url;

	@Column(length = 500, nullable = false)
	private String filePath;

	@Column(nullable = false)
	private String bucketName;

	@Column(nullable = false)
	private Long fileSize;

	@Column(nullable = false)
	private String fileType;
}
