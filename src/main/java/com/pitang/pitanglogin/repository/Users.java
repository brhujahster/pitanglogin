package com.pitang.pitanglogin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pitang.pitanglogin.model.User;

public interface Users extends JpaRepository<User, Long>{

		Optional<User> findByEmail(String email);
}
