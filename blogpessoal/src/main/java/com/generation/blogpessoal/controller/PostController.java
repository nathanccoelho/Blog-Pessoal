package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Post;
import com.generation.blogpessoal.repository.PostRepository;
import com.generation.blogpessoal.repository.ThemeRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private ThemeRepository themeRepository;
	
	
	@GetMapping
	public ResponseEntity<List<Post>> getAll(){
		return ResponseEntity.ok(postRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Post> getById(@PathVariable Long id){
		return  postRepository.findById(id).map(resposta-> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/title/{title}")
	public ResponseEntity<List<Post>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postRepository.findAllByTitleContainingIgnoreCase(titulo));
		}
	
	@PostMapping
	public ResponseEntity<Post> post(@Valid @RequestBody Post postagem){
		if(themeRepository.existsById(postagem.getTheme().getId()))
		return ResponseEntity.status(HttpStatus.CREATED).body(postRepository.save(postagem));
		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
	}
	
	@PutMapping
	public ResponseEntity<Post> put(@Valid @RequestBody Post postagem){
		
		if (postRepository.existsById(postagem.getId())) {
			
			if(themeRepository.existsById(postagem.getTheme().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(postRepository.save(postagem));
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete (@PathVariable Long id) {
		Optional <Post> postagem = postRepository.findById(id);
		
		if(postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		postRepository.deleteById(id);
		
	}
	
}
