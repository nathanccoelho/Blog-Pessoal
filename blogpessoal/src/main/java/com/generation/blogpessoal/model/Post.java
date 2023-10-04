package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



@Entity
@Table(name= "tb_post")
public class Post {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank (message = "O atributo title é Obrigatório!")
	@Size(min = 5, max = 100, message = "O atributo title deve conter no mínimo 05 e no máximo 100 caracteres")
	private String title;
	
	@NotBlank (message = "O atributo text é Obrigatório!")
	@Size(min = 10, max = 1000, message = "O atributo text deve conter no mínimo 10 e no máximo 1000 caracteres")
	private String text;
	
	@UpdateTimestamp
	private LocalDateTime date;
	
	@ManyToOne
	@JsonIgnoreProperties("post")
	private Theme theme;

	
	//Getters and Setters;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	
}