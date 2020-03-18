package br.com.brazilcode.cb.purchase.service.integration;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.User;

/**
 * Classe responsável por fazer a integração via REST com o serviço de Users do módulo Administration.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 17 de mar de 2020 22:37:11
 * @version 1.0
 */
@Service
public class UserIntegrationService implements Serializable {

	private static final long serialVersionUID = -804223068649349474L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserIntegrationService.class);

	@Autowired
	private Environment environment;

	/**
	 * Método responsável por fazer uma chamada via REST para o serviço de Users do módulo Administration.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return
	 * @throws RestClientException - Caso ocorra algum erro durante a comunicação com a API
	 * @throws ResourceNotFoundException - Caso o ID informado não exista na base de dados
	 */
	public User verifyIfExists(Long id) throws RestClientException, ResourceNotFoundException {
		final String method = "[ UserIntegrationService.verifyIfExist ] - ";
		LOGGER.debug(method + "BEGIN");

		final String url = environment.getProperty("cb.administration.service.url") + "users/" + id;
		final RestTemplate restTemplate = new RestTemplate();

		try {
			LOGGER.debug(method + "Calling the following URL: " + url);
			return restTemplate.getForObject(url, User.class);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		} finally {
			LOGGER.debug(method + "END");
		}
	}

}
