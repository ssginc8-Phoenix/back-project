package com.ssginc8.docto.user.service;

import com.ssginc8.docto.user.service.dto.AddDoctorList;
import com.ssginc8.docto.user.service.dto.AddUser;
import com.ssginc8.docto.user.service.dto.FindEmail;
import com.ssginc8.docto.user.service.dto.Login;
import com.ssginc8.docto.user.service.dto.SocialSignup;

public interface UserService {
	void checkEmail(String email);

	AddUser.Response createUser(AddUser.Request request);

	AddDoctorList.Response registerDoctor(AddDoctorList.Request request);

	SocialSignup.Response updateSocialInfo(SocialSignup.Request request);

	Login.Response login(Login.Request request);

	FindEmail.Response findEmail(FindEmail.Request request);
}
