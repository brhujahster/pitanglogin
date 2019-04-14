package com.pitang.pitanglogin.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pitang.pitanglogin.exceptions.InvalidFieldsException;
import com.pitang.pitanglogin.jwt.JwtAuthenticationRequest;
import com.pitang.pitanglogin.jwt.TokenUtil;
import com.pitang.pitanglogin.jwt.UserOfSystem;
import com.pitang.pitanglogin.model.User;
import com.pitang.pitanglogin.repository.Users;
import com.pitang.pitanglogin.service.UsersService;

@RestController
@CrossOrigin(origins = "*")
public class UsersResource {

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private Users users;
	@Autowired
	private TokenUtil tokenUtil;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody User user, BindingResult result) {
		JwtAuthenticationRequest authenticationRequest = new JwtAuthenticationRequest();
		authenticationRequest.setEmail(user.getEmail());
		authenticationRequest.setPassword(user.getPassword());
		if(result.hasErrors()) {
			throw new InvalidFieldsException("Invalid Fields");
		}
		usersService.save(user);
		
		return tokenUtil.returnTokenForUser(authenticationRequest);
	}
	
	@GetMapping("/me")
	public ResponseEntity<?> myProfile(@AuthenticationPrincipal UserOfSystem user) {
		User userLoged = users.returnAllOfUser(user.getUser().getId());
		return ResponseEntity.ok(userLoged);
	}
}
