package com.ssginc8.docto.user.service;

import java.io.IOException;

import com.ssginc8.docto.user.dto.AddUser;

public interface UserService {
	AddUser.Response createUser(AddUser.Request request) throws IOException;

	void checkEmail(String email);
}
