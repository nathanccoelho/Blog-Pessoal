package com.generation.blogpessoal.security;



import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/*É uma arquitetura que nos permite construir tokens para nossa aplicação.
 * A partir desse token, valida certas rotas para o usuário sem que o mesmo precise saber.
 * Ela cria o token e valida o mesmo.
 * 
 * */
@Component
public class JwtService {

	public static final String SECRET ="372795dea30c514b238727393d845372625661d4572e56fdc6883c5c7c2c76df";
	
	
	//É o secret codificado com a lógica.
	private Key getSignKey() {
		byte[]  keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	
	/*O claims é dentro de uma mensagem (token) enviar uma mensagem secreta.
	o token é aquele Bearer.
	Esse método extrai tudo que pode ser últil.
	*/
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey()).build()
				.parseClaimsJws(token).getBody();
	}
	
	
	/*Verifica usuário ou email, validação e tempo de expiração ou se ja expirou. 
	 * De uma maneira criptografada.*/
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String extractUsername (String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	
	/*Literalmente valida o token. o username é o mesmo que está no objeto
	 * userDetails. */
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	
	/*Ele constóis com o builder depois de extrair todas as informações do token, junta as infomações
	 * compacta tudo e constrói nosso token.
	 * O token nada mais é as informações do usuário criptografadas.*/
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}
	
	
	/*Após compactar as informações em um token criptografado chamamos esse método para gerar o token.*/
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}
	
	
}
