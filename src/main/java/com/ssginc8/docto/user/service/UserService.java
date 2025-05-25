package com.ssginc8.docto.user.service;

import com.ssginc8.docto.user.service.dto.AddDoctorList;
import com.ssginc8.docto.user.service.dto.AddUser;
import com.ssginc8.docto.user.service.dto.FindEmail;
import com.ssginc8.docto.user.service.dto.Login;
import com.ssginc8.docto.user.service.dto.SocialSignup;
import com.ssginc8.docto.user.service.dto.UserInfo;

public interface UserService {

	UserInfo.Response getMyInfo();

	void checkEmail(String email);

	FindEmail.Response findEmail(FindEmail.Request request);

	AddUser.Response createUser(AddUser.Request request);

	SocialSignup.Response updateSocialInfo(SocialSignup.Request request);

	AddDoctorList.Response registerDoctor(AddDoctorList.Request request);

	Login.Response login(Login.Request request);
}
