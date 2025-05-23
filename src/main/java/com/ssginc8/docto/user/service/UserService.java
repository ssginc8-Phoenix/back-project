package com.ssginc8.docto.user.service;

import com.ssginc8.docto.user.dto.AddUser;
import com.ssginc8.docto.user.dto.Login;
import com.ssginc8.docto.user.dto.SocialSignup;

public interface UserService {
	SocialSignup.Response updateSocialInfo(SocialSignup.Request request);

	AddUser.Response createUser(AddUser.Request request);

	void checkEmail(String email);

	Login.Response login(Login.Request request);
}
