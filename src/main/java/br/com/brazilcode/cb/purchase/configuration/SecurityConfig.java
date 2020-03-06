package br.com.brazilcode.cb.purchase.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import br.com.brazilcode.cb.purchase.filter.JWTAuthenticationFilter;
import br.com.brazilcode.cb.purchase.filter.JWTAuthorizationFilter;
import br.com.brazilcode.cb.purchase.service.CustomUserDetailService;

/**
 * Classe responsável por aplicar a camada de segurança JWT na aplicação.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 6 de mar de 2020 12:20:59
 * @version 1.0
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.csrf().disable().authorizeRequests()
				.antMatchers("/").permitAll()
				.anyRequest().authenticated()
				.and()
				// Filtrando outras requisições para checar se o JWT está adicionado ao header
				.addFilter(new JWTAuthenticationFilter(authenticationManager()))
				.addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailService));
	}

}
