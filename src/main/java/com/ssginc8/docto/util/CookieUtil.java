package com.ssginc8.docto.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

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

	public String getToken(HttpServletRequest request, String tokenType) {
		String token = null;

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(tokenType)) {
					token = cookie.getValue();
					break;
				}
			}
		}

		return token;
	}
}
