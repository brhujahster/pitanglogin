package com.pitang.pitanglogin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pitang.pitanglogin.model.TokenBlackList;

public interface TokensBlackList extends JpaRepository<TokenBlackList, Long>{

	Optional<TokenBlackList> findByToken(String token);
}
