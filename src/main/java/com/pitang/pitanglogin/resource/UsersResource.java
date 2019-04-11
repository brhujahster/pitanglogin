package com.pitang.pitanglogin.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pitang.pitanglogin.jwt.CurrentUser;
import com.pitang.pitanglogin.jwt.JwtTokenUtil;
import com.pitang.pitanglogin.model.User;
import com.pitang.pitanglogin.repository.Users;

@RestController
public class UsersResource {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private Users users;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody User user, BindingResult result) {
		
		if(result.hasErrors()) {
			System.out.println("----- Existem campos inválidos");
		}
		System.out.println("Usuario que chegou " + user);
		return null;
	}
	
	@GetMapping("/me")
	public void myProfile() {
		
	}
	
	private CurrentUser returnTokenForUser(User user) {
		
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						user.getEmail(), user.getPassword()
					)
			);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		final String token = jwtTokenUtil.generatedToken(userDetails);
		user.setPassword(null);
		
		return new CurrentUser(token, user);
	}
}
