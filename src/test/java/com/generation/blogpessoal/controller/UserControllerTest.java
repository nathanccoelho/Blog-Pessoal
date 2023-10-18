package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.User;
import com.generation.blogpessoal.repository.UserRepository;
import com.generation.blogpessoal.service.UserService;


/* A anotação Spring indica que a classe é uma classe teste.
 * webEnviromente indica que caso a porta 8080 esteja ocupada o Spring atribui outra porta automaticamente;*/
/* A anotação testInstance indica que o ciclo de vida da classe de teste será por classe;*/
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
	
	//Indica um objeto da classe testeRestTemplate para enviar requisições na nossa aplicação;
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	//Indica um objeto da classe UserService para persisteir os objetos no database de teste;
	@Autowired
	private UserService userService;
	
	//Indica um objeto da classe userRepository para limpar o database de testes;
	@Autowired
	private UserRepository userRepository;
	
	// A anotação BforeAll apaga todos os dados do database e cria o User Root para validações futuras;
	@BeforeAll
	void start() {
		userRepository.deleteAll();
		
		userService.registerUser(new User(0L, 
				"Root", "root@root.com", "rootroot", "-"));
	}
	
	
	@Test
	@DisplayName("Register User")
	public void mustCreateInUser() {
		HttpEntity<User> bodyRequest = new HttpEntity<User>(new User(0L, 
				"Paulo Antunes", "paulo_antunes@email.com.br", "123456789", "-"));
		
		ResponseEntity<User> bodyResponse = testRestTemplate
				.exchange("/users/register", HttpMethod.POST, bodyRequest, User.class);
		
		assertEquals(HttpStatus.CREATED, bodyResponse.getStatusCode());
	}
	
	@Test
	@DisplayName("Not register user duplicate")
	public void mustNotDuplicateUser() {
		userService.registerUser(new User(0L,
				"Maria da Silva", "maria_silva@email.com.br", "123456789", "-"));
		
		HttpEntity<User> bodyRequest = new HttpEntity<User>(new User(0L,
				"Maria da Silva", "maria_silva@email.com.br", "123456789", "-"));
		
		ResponseEntity<User> bodyResponse = testRestTemplate
				.exchange("/users/register", HttpMethod.POST, bodyRequest, User.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, bodyResponse.getStatusCode());
	}
	
	@Test
	@DisplayName("Update user")
	public void mustUpdateUser() {
		
		Optional<User> userRegistered = userService.registerUser(new User(0L,
				"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "-"));
		
		User userUpdate = new User(userRegistered.get().getId(),
				"Juliana Andrews Ramos", "juliana_andrews@email.com.br", "juliana123", "-");
		
		HttpEntity<User> bodyRequest = new HttpEntity<User>(userUpdate);
		
		ResponseEntity<User> bodyResponse = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/users/update", HttpMethod.PUT, bodyRequest, User.class);
		
		assertEquals(HttpStatus.OK, bodyResponse.getStatusCode());
	}
	
	@Test
	@DisplayName("List all users")
	public void mustListAllUsers() {
		
		userService.registerUser(new User(0L,
				"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "-"));
		
		userService.registerUser(new User(0L,
				"Ricardo Marques", "ricardo_marquer@email.com.br", "ricardo123", "-"));
				
		ResponseEntity<String> request = testRestTemplate
				.withBasicAuth("root@root.com", "rootroot")
				.exchange("/users/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, request.getStatusCode());
	}
	
	
	
	
	
	
	
	
	
	
	

}
