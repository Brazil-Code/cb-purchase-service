package br.com.brazilcode.cb.purchase.configuration;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe responsável por injetar o Bean Mapper.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 26 de mar de 2020 21:30:54
 * @version 1.0
 */
@Configuration
public class DozerConfig {

	@Bean
	public Mapper mapper() {
		return new DozerBeanMapper();
	}

}
