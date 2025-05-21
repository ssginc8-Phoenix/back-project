package com.ssginc8.docto.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;

@Component
public class CookieUtil {
	public Cookie createCookie(String cookieName, String cookieValue, int cookieMaxAge) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(cookieMaxAge);

		return cookie;
	}
}
