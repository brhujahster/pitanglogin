package com.pitang.pitanglogin.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.pitang.pitanglogin.model.User;

public class JwtUserFactory {

	private JwtUserFactory() {}
	
	
	public static JwtUser create(User user) {
		return new JwtUser(
				user.getId(), 
				user.getEmail(), 
				user.getPassword(), 
				mapToGranteAuthorities());
	}


	private static List<GrantedAuthority> mapToGranteAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}
}
