package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.User;

/*UserDetails(detalhes do usuário) é uma interface que trabalha com autenticação e libera autorizações para 
 * os usuários. 
 * Como um cracha de uma empresa que autentica a catraca.
 * Porém pra uma reunião somente quem tem autorização participará.*/
public class UserDetailsImpl implements UserDetails{

	
	//Identificador único para cada tipo de usuário, sistema de serelização.
	private static final long serialVersionUID= 1L;
	
	
	//Atributos para os usuários.
	private String userName;
	private String password;
	private List<GrantedAuthority> authorities;
	
	public UserDetailsImpl (User user) {
		this.userName = user.getUser();
		this.password = user.getPassword();
	}
	
	public UserDetailsImpl() {
		
	}

	
	//métodos sobre-escritos.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
