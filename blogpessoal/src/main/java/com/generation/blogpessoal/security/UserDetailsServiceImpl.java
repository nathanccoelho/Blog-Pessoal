package com.generation.blogpessoal.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.User;
import com.generation.blogpessoal.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements  UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUser(username);
		
		if(user.isPresent())
			return new UserDetailsImpl(user.get());
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);

	}
}
