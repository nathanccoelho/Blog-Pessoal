package com.generation.blogpessoal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.User;
import com.generation.blogpessoal.model.UserLogin;
import com.generation.blogpessoal.repository.UserRepository;
import com.generation.blogpessoal.security.JwtService;

@Service
public class UserService {

	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private String cryptoPassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}
	
	 private String generatedToken(String user) {
		 return "Bearer " + jwtService.generateToken(user);
	 }
	 
	 public Optional<User> registerUser(User user){
		 if(userRepository.findByUser(user.getUser()).isPresent())
			 return Optional.empty();
		 
		 user.setPassword(cryptoPassword(user.getPassword()));
		 
		 return Optional.of(userRepository.save(user));
	 }
	 
	 public Optional<User> updateUser(User user) {
		 if (userRepository.findById(user.getId()).isPresent()) {
			 Optional<User> buscarUser = userRepository.findByUser(user.getUser());
			 
			 if((buscarUser.isPresent()) && (buscarUser.get().getId() != user.getId())){
				 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user exists!", null);
			 }
			 
			 user.setPassword(cryptoPassword(user.getPassword()));
			 
			 return Optional.ofNullable(userRepository.save(user));
		 }
		 return Optional.empty();
	 }
	 
	 
	 public Optional<UserLogin> authenticationUser(Optional<UserLogin> userLogin){
		 var credential = new UsernamePasswordAuthenticationToken(userLogin.get().getUser(), userLogin.get().getPassword());
		 
		 org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(credential);
		 
		 if(authentication.isAuthenticated()) {
			 Optional<User> user = userRepository.findByUser(userLogin.get().getUser());
			 
			 if(user.isPresent()) {
				 userLogin.get().setId(user.get().getId());
				 userLogin.get().setName(user.get().getName());
				 userLogin.get().setImgUrl(user.get().getImgUrl());
				 userLogin.get().setToken(generatedToken(userLogin.get().getUser()));
				 userLogin.get().setPassword("");
				 
				 return userLogin;
			 }
		 }
		 
		 return Optional.empty();
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
