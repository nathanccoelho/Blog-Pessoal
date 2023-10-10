package com.generation.blogpessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.blogpessoal.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
	
	public Optional<User> findByUser(@Param("user") String user);

}
