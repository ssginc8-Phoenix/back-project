package com.ssginc8.docto.user.service;

import java.io.IOException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.file.dto.UploadFile;
import com.ssginc8.docto.file.entity.Category;
import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.file.service.FileService;
import com.ssginc8.docto.global.error.exception.fileException.FileUploadFailedException;
import com.ssginc8.docto.global.error.exception.userException.DuplicateEmailException;
import com.ssginc8.docto.user.dto.AddUser;
import com.ssginc8.docto.user.entity.LoginType;
import com.ssginc8.docto.user.entity.Role;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;
import com.ssginc8.docto.user.validator.CreateUserValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private final FileService fileService;
	private final UserProvider userProvider;
	private final CreateUserValidator createUserValidator;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional
	public AddUser.Response createUser(AddUser.Request request) {
		// 1. 이메일 중복 검사, 패스워드 검증
		createUserValidator.validate(request);

		// 2. 패스워드 암호화, 주민번호 암호화
		String encryptedPassword = bCryptPasswordEncoder.encode(request.getPassword());

		File profileImage = null;

		// 3. 프로필 사진이 있는 경우 -> s3에 저장
		if (!request.getProfileImage().isEmpty()) {
			UploadFile.Request uploadFile = UploadFile.Request.builder()
				.file(request.getProfileImage())
				.category(Category.USER)
				.build();

			try {
				profileImage = fileService.uploadImage(uploadFile);
				log.info("--------------------file id-----------------");
				log.info(profileImage.getFileId());
			} catch (IOException e) {
				throw new FileUploadFailedException();
			}
		}

		// 4. User 엔티티 생성
		User user = User.createUser(request.getEmail(), encryptedPassword, request.getName(),
			request.getPhone(), request.getAddress(), LoginType.EMAIL, Role.valueOf(request.getRole()),
			profileImage);

		// 5. user 저장 후 만들어진 user id 반환
		return AddUser.Response.builder()
			.userId(userProvider.createUser(user).getUserId())
			.build();
	}

	@Override
	public void checkEmail(String email) {
		if (userProvider.checkEmail(email).isPresent()) {
			throw new DuplicateEmailException();
		}
	}
}
