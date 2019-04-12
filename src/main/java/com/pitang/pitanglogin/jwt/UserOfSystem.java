package com.pitang.pitanglogin.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserOfSystem extends User{

	private static final long serialVersionUID = 1L;
	private com.pitang.pitanglogin.model.User user;
	
	public UserOfSystem(com.pitang.pitanglogin.model.User user, Collection<? extends GrantedAuthority> authorities) {
		super(user.getEmail(), user.getPassword(), authorities);
		this.user = user;
	}
	
	public com.pitang.pitanglogin.model.User getUser() {
		return user;
	}

}
