package com.ssginc8.docto.user.service;

import com.ssginc8.docto.user.dto.AddUser;

public interface UserService {
	AddUser.Response createUser(AddUser.Request request);

	void checkEmail(String email);
}
