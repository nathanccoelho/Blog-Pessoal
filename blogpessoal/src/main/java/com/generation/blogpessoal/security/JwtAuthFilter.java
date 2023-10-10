package com.generation.blogpessoal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*Essa classe faz uma serie de filtros e validações baseado nas variáeis que declaramos ali.
 * os headers é um metadado para capturar nas requisições. É um parametro que vem pelo cabeçario da infomação.
 * Através pel JSON.
 * 
 * 
 * */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
										FilterChain filterChain) throws ServletException, IOException, java.io.IOException{
		
		/*captura o token e faz os teste para validações.*/
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;
		
		try {
			if(authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
				username = jwtService.extractUsername(token);
			}
			
			/*Verifica o username e através do token cria o objeto userDetails.*/
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				//Ocorre a autenticação do token.
				if (jwtService.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			//Compartilha a informação do nosso token para o restante do código. e o catch é as tratativa de erro.
			
			filterChain.doFilter(request, response);
			
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | ResponseStatusException e) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}
		
	}
}
