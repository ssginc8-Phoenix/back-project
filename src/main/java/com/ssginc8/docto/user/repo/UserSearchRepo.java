package com.ssginc8.docto.user.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ssginc8.docto.user.entity.Role;
import com.ssginc8.docto.user.entity.User;

public interface UserSearchRepo {
	Page<User> findByRoleAndDeletedAtIsNotNull(Role role, Pageable pageable);
}
