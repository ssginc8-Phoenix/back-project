package com.ssginc8.docto.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssginc8.docto.auth.jwt.dto.Token;
import com.ssginc8.docto.auth.jwt.entity.RefreshToken;
import com.ssginc8.docto.auth.jwt.provider.RefreshTokenProvider;
import com.ssginc8.docto.auth.jwt.provider.TokenProvider;
import com.ssginc8.docto.file.dto.UploadFile;
import com.ssginc8.docto.file.entity.Category;
import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.file.service.FileService;
import com.ssginc8.docto.global.error.exception.userException.DuplicateEmailException;
import com.ssginc8.docto.global.error.exception.userException.InvalidPasswordException;
import com.ssginc8.docto.user.dto.AddDoctorList;
import com.ssginc8.docto.user.dto.AddUser;
import com.ssginc8.docto.user.dto.Login;
import com.ssginc8.docto.user.dto.SocialSignup;
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
	private final TokenProvider tokenProvider;
	private final RefreshTokenProvider refreshTokenProvider;

	@Override
	public void checkEmail(String email) {
		if (userProvider.checkEmail(email).isPresent()) {
			throw new DuplicateEmailException();
		}
	}

	@Transactional
	@Override
	public AddUser.Response createUser(AddUser.Request request) {
		// 1. 이메일 중복 검사, 패스워드 검증
		createUserValidator.validate(request);

		// 2. 패스워드 암호화, 주민번호 암호화
		String encryptedPassword = bCryptPasswordEncoder.encode(request.getPassword());

		// 프로필 이미지 있는 경우 S3에 업로드
		File profileImage = uploadProfileImage(request.getProfileImage());

		// 4. User 엔티티 생성
		User user = User.createUserByEmail(request.getEmail(), encryptedPassword, request.getName(), request.getPhone(),
			Role.valueOf(request.getRole()), profileImage);

		user = userProvider.createUser(user);

		log.info(user.getRole().getKey());

		log.info(Role.valueOf(request.getRole()).getKey());

		// 5. user 저장 후 만들어진 user id 반환
		return AddUser.Response.builder()
			.userId(user.getUserId())
			.role(user.getRole().getKey())
			.build();
	}

	@Transactional
	@Override
	public SocialSignup.Response updateSocialInfo(SocialSignup.Request request) {
		// 프로필 사진 있는 경우 s3에 저장
		File profileImage = uploadProfileImage(request.getProfileImage());

		// userId로 사용자 찾이오기 -> provider
		User user = userProvider.loadUserByProviderId(request.getProviderId());

		// user 정보 업데이트
		user.updateSocialInfo(request.getPhone(), profileImage, Role.valueOf(request.getRole()));

		// response 만들어서 반환
		return SocialSignup.Response.builder()
			.userId(user.getUserId())
			.role(user.getRole().getKey())
			.build();
	}

	@Transactional
	@Override
	public AddDoctorList.Response registerDoctor(AddDoctorList.Request request) {

		List<Long> ids = new ArrayList<>();

		for (AddDoctorList.CreateDoctor doctor : request.getCreateDoctors()) {
			createUserValidator.validateEmail(doctor.getEmail());
			String encryptedPassword = bCryptPasswordEncoder.encode(doctor.getPassword());

			User user = User.createUserByEmail(doctor.getEmail(), encryptedPassword,
				doctor.getName(), doctor.getPhone(), Role.DOCTOR, null);

			ids.add(userProvider.createUser(user).getUserId());
		}

		return AddDoctorList.Response.builder()
			.ids(ids)
			.build();
	}

	@Transactional
	@Override
	public Login.Response login(Login.Request request) {
		User user = userProvider.loadUserByEmail(request.getEmail())
			.orElseThrow(DuplicateEmailException::new);

		if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidPasswordException();
		}

		Token tokens = tokenProvider.generateTokens(
			user.getUuid(),
			user.getRole().getKey()
		);

		RefreshToken refreshToken = refreshTokenProvider.findByUuid(user.getUuid());

		if (refreshToken != null) {
			refreshToken.update(tokens.getRefreshToken());
		} else {
			refreshToken = RefreshToken.createRefreshToken(user.getUuid(), tokens.getRefreshToken());
			refreshTokenProvider.saveRefreshToken(refreshToken);
		}

		return Login.Response.builder()
			.accessToken(tokens.getAccessToken())
			.refreshToken(tokens.getRefreshToken())
			.accessTokenCookieMaxAge(tokens.getAccessTokenCookieMaxAge())
			.refreshTokenCookieMaxAge(tokens.getRefreshTokenCookieMaxAge())
			.build();
	}

	// 프로필 사진이 있는 경우 -> s3에 저장
	private File uploadProfileImage(MultipartFile profileImage) {

		File savedProfileImage = null;

		if (Objects.nonNull(profileImage)) {
			UploadFile.Request fileRequest = UploadFile.Request.builder()
				.file(profileImage)
				.category(Category.USER)
				.build();

			UploadFile.Response fileResponse = fileService.uploadImage(fileRequest);

			savedProfileImage = File.createFile(fileResponse.getCategory(), fileResponse.getFileName(),
				fileResponse.getOriginalFileName(),
				fileResponse.getUrl(), fileResponse.getBucket(), fileResponse.getFileSize(),
				fileResponse.getFileType());
		}

		return savedProfileImage;
	}
}
