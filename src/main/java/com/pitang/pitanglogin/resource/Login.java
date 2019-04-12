package com.pitang.pitanglogin.resource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pitang.pitanglogin.jwt.CurrentUser;
import com.pitang.pitanglogin.jwt.JwtAuthenticationRequest;
import com.pitang.pitanglogin.jwt.JwtTokenUtil;
import com.pitang.pitanglogin.model.User;
import com.pitang.pitanglogin.repository.Users;

@RestController
public class Login {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private Users users;
	
	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody JwtAuthenticationRequest jwtAuthenticationRequest) {
		User userRecuperado = null;
		
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(jwtAuthenticationRequest.getEmail(), jwtAuthenticationRequest.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtAuthenticationRequest.getEmail());
		final String token = jwtTokenUtil.generatedToken(userDetails);
		final Optional<User> user = users.findByEmail(jwtAuthenticationRequest.getEmail());
		if(user.isPresent()) {
			userRecuperado = user.get();
		}
		userRecuperado.setPassword(null);
		return ResponseEntity.ok(new CurrentUser(token, userRecuperado));
	}
}
