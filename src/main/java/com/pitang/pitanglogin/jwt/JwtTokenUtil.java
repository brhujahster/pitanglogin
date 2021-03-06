package com.pitang.pitanglogin.jwt;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.pitang.pitanglogin.model.TokenBlackList;
import com.pitang.pitanglogin.repository.TokensBlackList;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable{

	private static final long serialVersionUID = 6757351875868335243L;
	
	private final String CLAIM_KEY_USERNAME = "sub";
	private final String CLAIM_KEY_CREATED = "created";
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	@Autowired
	private TokensBlackList tokensBlackList;
	
	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims= getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		
		return username;
	}
	
	public Date getExpirationDataFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		
		return expiration;
	}
	
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		
		try {
			claims = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
		} catch(Exception e) {
			claims = null;
		}
		
		return claims;		
	}
	
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDataFromToken(token);
		if(expiration == null) {
			return true;
		}
		return expiration.before(new Date());
	}
	
	public String generatedToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		final Date createdDate = new Date();
		claims.put(CLAIM_KEY_CREATED, createdDate);
		
		return doGenerateToken(claims);
	}
	
	private String doGenerateToken(Map<String, Object> claims) {
		final Date createDate = (Date) claims.get(CLAIM_KEY_CREATED);
		final Date expirationDate = new Date(createDate.getTime() + expiration * 1000);
		
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	
	
	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token));
	}
	
	public String refreshToken(String token) {
		String refreshedToken;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = doGenerateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		
		return refreshedToken;
	}
	
	public Boolean validateToken(String token, UserDetails userDetails, HttpServletResponse response) throws IOException {
		validatedTokenInBlackList(token, response);
		
		UserOfSystem user = (UserOfSystem) userDetails;
		
		final String username = getUsernameFromToken(token);
		return username.equals(user.getUsername());
	}
	
	
	private void validatedTokenInBlackList(String token, HttpServletResponse response) throws IOException {
		Optional<TokenBlackList> tokenSearched = tokensBlackList.findByToken(token);
		if(tokenSearched.isPresent()) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - invalid Token");
		}
	}
	
	
	
	
	
	
	
}
