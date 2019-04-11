package com.pitang.pitanglogin.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.pitang.pitanglogin.model.User;
import com.softengine.helpdesk.api.enums.ProfileEnum;

public class JwtUserFactory {

	private JwtUserFactory() {}
	
	
	public static JwtUser create(User user) {
		return new JwtUser(
				user.getId(), 
				user.getEmail(), 
				user.getPassword(), 
				mapToGranteAuthorities(user.getProfile()));
	}


	private static List<GrantedAuthority> mapToGranteAuthorities(ProfileEnum profile) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(profile.toString()));
		return authorities;
	}
}
