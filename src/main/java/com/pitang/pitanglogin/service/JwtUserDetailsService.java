package com.pitang.pitanglogin.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pitang.pitanglogin.jwt.JwtUserFactory;
import com.pitang.pitanglogin.model.User;
import com.pitang.pitanglogin.repository.Users;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	@Autowired
	private Users users;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = users.findByEmail(email);
		
		if(user.isPresent()) {
			return JwtUserFactory.create(user.get());
		} else {
			throw new UsernameNotFoundException(String.format("O usuário %s não foi encontrado", email));
		}
		
	}

}
