package com.ssginc8.docto.user.validator;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.global.error.exception.userException.DuplicateEmailException;
import com.ssginc8.docto.global.error.exception.userException.PasswordHasSequenceException;
import com.ssginc8.docto.global.error.exception.userException.PasswordTooShortException;
import com.ssginc8.docto.global.error.exception.userException.PasswordTooSimpleException;
import com.ssginc8.docto.user.dto.AddUser;
import com.ssginc8.docto.user.provider.UserProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CreateUserValidator {
	private final UserProvider userProvider;

	// 이메일 + 비밀번호 검사 메서드
	public void validate(AddUser.Request request) {
		checkEmail(request.getEmail());
		checkPassword(request.getPassword());
	}

	// 이메일 중복 검증 메서드
	private void checkEmail(String email) {
		if (userProvider.checkEmail(email).isPresent()) {
			throw new DuplicateEmailException();
		}
	}

	// 비밀번호 검증 메서드
	private void checkPassword(String password) {
		if (password == null || password.length() < 8) {
			throw new PasswordTooShortException();
		}

		int typeCount = 0;
		if (password.matches(".*[A-Z].*"))
			typeCount++; // 대문자
		if (password.matches(".*[a-z].*"))
			typeCount++; // 소문자
		if (password.matches(".*[0-9].*"))
			typeCount++; // 숫자
		if (password.matches(".*[!@#$%^&*()_+\\-={}|\\[\\]:\";'<>?,./`~].*"))
			typeCount++; // 특수문자

		if (typeCount < 2) {
			throw new PasswordTooSimpleException();
		}

		// 연속된 키보드 문자열 제한
		String[] badSequences = {
			"abcdefghijklmnopqrstuvwxyz", "qwertyuiop", "asdfghjkl", "zxcvbnm",
			"0123456789"
		};
		String lowerPassword = password.toLowerCase();

		for (String seq : badSequences) {
			for (int i = 0; i < seq.length() - 3; i++) {
				String subSeq = seq.substring(i, i + 4);
				if (lowerPassword.contains(subSeq)) {
					throw new PasswordHasSequenceException();
				}
			}
		}
	}

}
