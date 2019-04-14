package com.pitang.pitanglogin.jwt;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.pitang.pitanglogin.model.User;
import com.pitang.pitanglogin.repository.Users;

@Component
public class TokenUtil {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private Users users;
	
	public ResponseEntity<?> returnTokenForUser(JwtAuthenticationRequest authenticationRequest) {
		User userRecuperado = null;
		
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.generatedToken(userDetails);
		final Optional<User> userOptional = users.findByEmail(authenticationRequest.getEmail());
		if(userOptional.isPresent()) {
			userRecuperado = userOptional.get();
		}
		userRecuperado.setPassword(null);
		return ResponseEntity.ok(new CurrentUser(token, userRecuperado));
	}
}
