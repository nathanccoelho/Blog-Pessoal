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
/*É uma classe que reutiliza alguns códigos e trabalha com regras de negócios.
Serve para manipular.
*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	//Injeção de dependencia.
	@Autowired
	private UserRepository userRepository;
	
	//Verificar se o usuário existe no banco de dados.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUser(username);
		
		if(user.isPresent())
			return new UserDetailsImpl(user.get());
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);

	}
}
