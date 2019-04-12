package com.pitang.pitanglogin.resource;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pitang.pitanglogin.jwt.CurrentUser;
import com.pitang.pitanglogin.jwt.JwtAuthenticationRequest;
import com.pitang.pitanglogin.jwt.JwtTokenUtil;
import com.pitang.pitanglogin.jwt.UserOfSystem;
import com.pitang.pitanglogin.model.User;
import com.pitang.pitanglogin.repository.Users;
import com.pitang.pitanglogin.service.UsersService;

@RestController
public class UsersResource {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private Users users;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody User user, BindingResult result) {
		JwtAuthenticationRequest authenticationRequest = new JwtAuthenticationRequest();
		authenticationRequest.setEmail(user.getEmail());
		authenticationRequest.setPassword(user.getPassword());
		if(result.hasErrors()) {
			System.out.println("----- Existem campos inválidos");
		}
		usersService.save(user);
		
		System.out.println("Usuario que chegou " + user);
		return returnTokenForUser(authenticationRequest);
	}
	
	@GetMapping("/me")
	public ResponseEntity<?> myProfile(@AuthenticationPrincipal UserOfSystem user) {
		User userLoged = users.returnAllOfUser(user.getUser().getId());
		System.out.println("------- id do usuario " + userLoged.getId());
		return ResponseEntity.ok(userLoged);
	}
	
	private ResponseEntity<?> returnTokenForUser(JwtAuthenticationRequest authenticationRequest) {
		User userRecuperado = null;
		
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final String token = jwtTokenUtil.generatedToken(userDetails);
		final Optional<User> user2 = users.findByEmail(authenticationRequest.getEmail());
		if(user2.isPresent()) {
			userRecuperado = user2.get();
		}
		userRecuperado.setPassword(null);
		return ResponseEntity.ok(new CurrentUser(token, userRecuperado));
	}
}
