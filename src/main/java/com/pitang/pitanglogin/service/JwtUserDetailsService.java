package com.pitang.pitanglogin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pitang.pitanglogin.jwt.UserOfSystem;
import com.pitang.pitanglogin.model.User;
import com.pitang.pitanglogin.repository.Users;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	@Autowired
	private Users users;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userOptional = users.findByEmail(email);
		
		User user = userOptional.orElseThrow(() -> 
			new UsernameNotFoundException(String.format("O usuário %s não foi encontrado", email)));
		
		return new UserOfSystem(user, mapToGranteAuthorities());
	}
	
	private static List<GrantedAuthority> mapToGranteAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}

}
