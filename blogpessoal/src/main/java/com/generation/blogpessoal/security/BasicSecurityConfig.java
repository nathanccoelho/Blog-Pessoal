package com.generation.blogpessoal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*Ela vem do Spring. A partir da Basic ela vai chamando as outras classes.*/
@Configuration
@EnableWebSecurity
public class BasicSecurityConfig {

	//Habilitação do filtro.
	@Autowired
	private JwtAuthFilter authFilter;
	
	//habilita o UserDetailsService.
	
	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    //Espaço de autenticação. O provedor pergunta o modelo e passamos pra ele no código. E criptografa a senha.
    @Bean
    AuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    	authenticationProvider.setUserDetailsService(userDetailsService());
    	authenticationProvider.setPasswordEncoder(passwordEncoder());
    	return authenticationProvider;
    }
    
    //Ele gerencia o espaço de autenticação. Controla as informações basicamente.
    @Bean
    AuthenticationManager authenticatioManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	return authenticationConfiguration.getAuthenticationManager();
    }
    
    //O filterChain é chamado no Filter. 
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    	//Verifica o estado de sessão se ele desloga ou não o usuário. Para segurança do mesmo. 
        http
        		.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .cors();

        http
        		.authorizeHttpRequests((auth) -> auth
              	        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic();

        return http.build();

    }
    
    
    
    
    
    
    
    
    
}
