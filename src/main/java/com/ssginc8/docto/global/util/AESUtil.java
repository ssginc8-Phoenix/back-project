package com.ssginc8.docto.global.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {
	private static final String ALGORITHM = "AES";
	private static final String SECRET_KEY = "1234567890123456"; // 꼭 16, 24, 32자 길이여야 해

	public static String encrypt(String plainText) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encrypted = cipher.doFinal(plainText.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			throw new RuntimeException("Error while encrypting", e);
		}
	}

	public static String decrypt(String encryptedText) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
			byte[] decrypted = cipher.doFinal(decodedBytes);
			return new String(decrypted);
		} catch (Exception e) {
			throw new RuntimeException("Error while decrypting", e);
		}
	}
}
