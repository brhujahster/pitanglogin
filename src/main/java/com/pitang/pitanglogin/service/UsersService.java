package com.pitang.pitanglogin.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pitang.pitanglogin.exceptions.EmailAlreadyExistsException;
import com.pitang.pitanglogin.model.User;
import com.pitang.pitanglogin.repository.Phones;
import com.pitang.pitanglogin.repository.Users;

@Service
public class UsersService {

	@Autowired
	private Users users;
	
	@Autowired
	private Phones phones;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User save(User user) {
		if(user.getId() != null) {
			return updateUser(user);
		}	else {
			return saveNewUser(user);
		}
	}
	
	private User saveNewUser(User user) {
		if(isUserExist(user)) {
			throw new EmailAlreadyExistsException("E-mail already exists");
		}
		
		user.setCreatedAt(LocalDate.now());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User userSaved = users.save(user);
		
		savePhonesForUser(userSaved);
		
		return users.save(user);
	}
	
	private User updateUser(User user) {
		return users.save(user);
	}
	
	public boolean logout(User user) {
		LocalDate logoutDate = LocalDate.now();
		user.setLastLogin(logoutDate);
		User userUpdated = users.save(user);
		if(userUpdated != null) {
			return true;
		}
		return false;
	}
	
	private boolean isUserExist(User user) {
		return users.findByEmail(user.getEmail()).isPresent();
	}
	
	private void savePhonesForUser(User user) {
		user.getPhones().forEach( f -> {
			f.setUser(user);
			phones.save(f);
		});
	}
	
}
