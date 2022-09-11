package com.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	public Optional<User> findUserByEmail(String email);
	
	@Query("select s from User s where s.email=:email")
	public User getUserByEmail(@Param("email") String email);
}
