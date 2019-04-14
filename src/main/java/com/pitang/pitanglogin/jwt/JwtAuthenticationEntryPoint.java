package com.pitang.pitanglogin.jwt;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable{

	private static final long serialVersionUID = 4192824009390152986L;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		String authToken = request.getHeader("Authorization");
		if(jwtTokenUtil.isTokenExpired(authToken)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - invalid session");
		}else {			
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		}
	}

}
