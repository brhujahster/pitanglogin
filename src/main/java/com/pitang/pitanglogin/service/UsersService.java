package com.pitang.pitanglogin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pitang.pitanglogin.exceptions.EmailAlreadyExistsException;
import com.pitang.pitanglogin.model.User;
import com.pitang.pitanglogin.repository.Users;

@Service
public class UsersService {

	@Autowired
	private Users users;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User save(User user) {
		if(isUserExist(user)) {
			throw new EmailAlreadyExistsException("E-mail already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return users.save(user);
	}
	
	private boolean isUserExist(User user) {
		return users.findByEmail(user.getEmail()).isPresent();
	}
	
}
