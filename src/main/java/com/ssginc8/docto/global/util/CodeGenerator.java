package com.ssginc8.docto.global.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class CodeGenerator {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int CODE_LENGTH = 8;
	private static final SecureRandom random = new SecureRandom();

	public static String generateCode() {
		StringBuilder code = new StringBuilder();

		for (int i = 0; i < CODE_LENGTH; i++) {
			int index = random.nextInt(CHARACTERS.length());
			code.append(CHARACTERS.charAt(index));
		}

		return code.toString();
	}
}
