package com.pitang.pitanglogin.resource;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pitang.pitanglogin.jwt.JwtAuthenticationRequest;
import com.pitang.pitanglogin.jwt.TokenUtil;
import com.pitang.pitanglogin.jwt.UserOfSystem;
import com.pitang.pitanglogin.model.TokenBlackList;
import com.pitang.pitanglogin.model.User;
import com.pitang.pitanglogin.repository.TokensBlackList;
import com.pitang.pitanglogin.service.UsersService;

@RestController
@CrossOrigin(origins = "*")
public class Login {

	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private UsersService userService;
	
	@Autowired
	private TokensBlackList  tokensBlackList;
	
	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody JwtAuthenticationRequest jwtAuthenticationRequest) {
		return tokenUtil.returnTokenForUser(jwtAuthenticationRequest);
	}
	
	@GetMapping("/leave") 
	public ResponseEntity<?> logout(@AuthenticationPrincipal UserOfSystem userOfSystem, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String authToken = request.getHeader("Authorization");
		TokenBlackList tokenBlackList = new TokenBlackList();
		tokenBlackList.setToken(authToken);
		tokensBlackList.save(tokenBlackList);
		
		User user = userOfSystem.getUser();
		user.setLastLogin(LocalDate.now());
		userService.save(user);
		return ResponseEntity.ok("Logout Successful");
	}
}
