package com.pitang.pitanglogin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pitang.pitanglogin.model.User;

public interface Users extends JpaRepository<User, Long>{

		Optional<User> findByEmail(String email);
		
		@Query("SELECT u FROM User u left join u.phones p where u.id = ?1")
		User returnAllOfUser(Long id);
}
