package br.com.brazilcode.cb.purchase.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Classe responsável por habilitar assincronidade na aplicação.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 31 de mar de 2020 15:11:26
 * @version 1.0
 */
@Configuration
@EnableAsync
public class AsyncConfig {

	/**
	 * Método responsável por configurar a disponibilização de um nova Thread para métodos assíncronos.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @return
	 */
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(5);
		pool.setMaxPoolSize(10);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		return pool;
	}

}
