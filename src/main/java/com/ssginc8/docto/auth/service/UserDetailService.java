package com.ssginc8.docto.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.auth.jwt.adapter.UserDetailAdapter;
import com.ssginc8.docto.user.provider.UserProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
// 스프링 시큐리티에서 사용자 정보 가져오는 인터페이스
public class UserDetailService implements UserDetailsService {
	private final UserProvider userProvider;

	@Override // 사용자 이름으로 사용자 정보 가져옴.
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return new UserDetailAdapter(userProvider.loadUserByEmailOrException(email));
	}
}
