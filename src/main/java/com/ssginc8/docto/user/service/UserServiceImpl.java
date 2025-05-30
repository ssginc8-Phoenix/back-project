package com.ssginc8.docto.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssginc8.docto.auth.jwt.dto.Token;
import com.ssginc8.docto.auth.jwt.entity.RefreshToken;
import com.ssginc8.docto.auth.jwt.provider.RefreshTokenProvider;
import com.ssginc8.docto.auth.jwt.provider.TokenProvider;
import com.ssginc8.docto.file.entity.Category;
import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.file.service.FileService;
import com.ssginc8.docto.file.service.dto.UpdateFile;
import com.ssginc8.docto.file.service.dto.UploadFile;
import com.ssginc8.docto.global.error.exception.userException.UserNotFoundException;
import com.ssginc8.docto.global.event.EmailSendEvent;
import com.ssginc8.docto.global.util.CodeGenerator;
import com.ssginc8.docto.global.util.RedisKeyPrefix;
import com.ssginc8.docto.global.util.RedisUtil;
import com.ssginc8.docto.user.entity.Role;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;
import com.ssginc8.docto.user.repo.UserSearchRepoImpl;
import com.ssginc8.docto.user.service.dto.AddDoctorList;
import com.ssginc8.docto.user.service.dto.AddUser;
import com.ssginc8.docto.user.service.dto.AdminUserList;
import com.ssginc8.docto.user.service.dto.EmailVerification;
import com.ssginc8.docto.user.service.dto.FindEmail;
import com.ssginc8.docto.user.service.dto.Login;
import com.ssginc8.docto.user.service.dto.ResetPassword;
import com.ssginc8.docto.user.service.dto.SendVerifyCode;
import com.ssginc8.docto.user.service.dto.SocialSignup;
import com.ssginc8.docto.user.service.dto.UpdateUser;
import com.ssginc8.docto.user.service.dto.UserInfo;
import com.ssginc8.docto.user.validator.UserValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private final FileService fileService;
	private final UserProvider userProvider;
	private final UserValidator userValidator;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final TokenProvider tokenProvider;
	private final RefreshTokenProvider refreshTokenProvider;
	private final UserSearchRepoImpl userSearchRepoImpl;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final RedisUtil redisUtil;

	@Transactional(readOnly = true)
	@Override
	public UserInfo.Response getMyInfo() {
		User user = getUserFromUuid();

		return UserInfo.Response.from(user);
	}

	@Transactional(readOnly = true)
	@Override
	public FindEmail.Response findEmail(FindEmail.Request request) {
		User user = userProvider.loadEmailByNameAndPhone(request.getName(), request.getPhone());

		return FindEmail.Response.builder()
			.email(user.getEmail())
			.build();
	}

	@Override
	public Page<AdminUserList.Response> getUsers(Role role, Pageable pageable) {
		Page<User> users = userSearchRepoImpl.findByRoleAndDeletedAtIsNull(role, pageable);

		List<AdminUserList.Response> userList = users.stream()
			.map(AdminUserList.Response::from).toList();

		return new PageImpl<>(userList, pageable, users.getTotalElements());
	}

	@Transactional(readOnly = true)
	@Override
	public void checkEmail(String email) {
		userValidator.validateEmail(email);
	}

	@Transactional
	@Override
	public AddUser.Response createUser(AddUser.Request request) {
		// 1. 이메일 중복 검사, 패스워드 검증
		userValidator.validate(request);

		// 2. 패스워드 암호화, 주민번호 암호화
		String encryptedPassword = bCryptPasswordEncoder.encode(request.getPassword());

		// 프로필 이미지 있는 경우 S3에 업로드
		File profileImage = uploadProfileImage(request.getProfileImage());

		// 4. User 엔티티 생성
		User user = User.createUserByEmail(request.getEmail(), encryptedPassword, request.getName(), request.getPhone(),
			request.getAddress(), request.getRole(), profileImage);

		user = userProvider.createUser(user);

		// 5. user 저장 후 만들어진 user id 반환
		return AddUser.Response.builder()
			.userId(user.getUserId())
			.role(user.getRole())
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

		for (AddDoctorList.DoctorInfo doctor : request.getDoctorInfos()) {
			userValidator.validateEmail(doctor.getEmail());
			String encryptedPassword = bCryptPasswordEncoder.encode(doctor.getPassword());

			User user = User.createDoctorByEmail(doctor.getEmail(), encryptedPassword,
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
			.orElseThrow(UserNotFoundException::new);

		userValidator.isPasswordMatch(request.getPassword(), user.getPassword());

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

	@Transactional
	@Override
	public void sendVerificationCode(SendVerifyCode.Request request) {
		String code = CodeGenerator.generateCode();
		String toEmail = request.getEmail();

		redisUtil.createRedisData(RedisKeyPrefix.EMAIL_AUTH.key(toEmail), code, RedisKeyPrefix.EMAIL_AUTH.getTtl());

		applicationEventPublisher.publishEvent(EmailSendEvent.emailVerification(toEmail, code));
	}

	@Override
	public void confirmVerificationCode(EmailVerification.Request request) {
		String storedCode = redisUtil.getData(RedisKeyPrefix.EMAIL_AUTH.key(request.getEmail()));

		userValidator.validateCode(request.getCode(), storedCode);
	}

	@Transactional
	@Override
	public void resetPassword(ResetPassword.Request request) {
		User user = userProvider.loadUserByEmailOrException(request.getEmail());

		userValidator.validatePassword(user.getPassword(), request.getPassword());

		user.updatePassword(bCryptPasswordEncoder.encode(request.getPassword()));
	}

	@Transactional
	@Override
	public void updateInfo(UpdateUser.Request request) {
		User user = getUserFromUuid();

		userValidator.validateUpdateEmail(request.getEmail(), user.getUserId());

		File file = null;

		if (Objects.nonNull(request.getProfileImage())) {
			String originalProfileName = null;
			if (Objects.nonNull(user.getProfileImage())) {
				originalProfileName = user.getProfileName();
			}

			UpdateFile.Command command = UpdateFile.Command.builder()
				.originalFileName(originalProfileName)
				.category(Category.USER)
				.file(request.getProfileImage())
				.build();

			UpdateFile.Result result = fileService.updateFile(command);

			file = File.createFile(result.getCategory(), result.getFileName(),
				result.getOriginalFileName(),
				result.getUrl(), result.getBucket(), result.getFileSize(),
				result.getFileType());
		}

		user.updateUser(request.getName(), request.getEmail(), request.getPhone(), request.getAddress(), file);
	}

	@Transactional
	@Override
	public void deleteAccount() {
		User user = getUserFromUuid();

		user.delete();
	}

	// 프로필 사진이 있는 경우 -> s3에 저장
	private File uploadProfileImage(MultipartFile profileImage) {

		File savedProfileImage = null;

		if (Objects.nonNull(profileImage)) {
			UploadFile.Command fileCommand = UploadFile.Command.builder()
				.file(profileImage)
				.category(Category.USER)
				.build();

			UploadFile.Result fileResult = fileService.uploadImage(fileCommand);

			savedProfileImage = File.createFile(fileResult.getCategory(), fileResult.getFileName(),
				fileResult.getOriginalFileName(),
				fileResult.getUrl(), fileResult.getBucket(), fileResult.getFileSize(),
				fileResult.getFileType());
		}

		return savedProfileImage;
	}

	public User getUserFromUuid() {
		String uuid = SecurityContextHolder.getContext().getAuthentication().getName();

		return userProvider.loadUserByUuid(uuid);
	}
}
