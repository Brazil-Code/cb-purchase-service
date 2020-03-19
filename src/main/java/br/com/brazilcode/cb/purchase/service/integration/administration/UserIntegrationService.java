package br.com.brazilcode.cb.purchase.service.integration.administration;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.User;

/**
 * Classe responsável por fazer a integração via REST com o serviço de Users do módulo Administration.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 17 de mar de 2020 22:37:11
 * @version 1.1
 */
@Service
public class UserIntegrationService implements Serializable {

	private static final long serialVersionUID = -804223068649349474L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserIntegrationService.class);

	@Autowired
	private Environment environment;

	/**
	 * Método responsável por fazer uma chamada via REST para o serviço de busca de Users do módulo Administration.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return
	 * @throws RestClientException - Caso ocorra algum erro durante a comunicação com a API
	 * @throws ResourceNotFoundException - Caso o ID informado não exista na base de dados
	 */
	public User verifyIfExists(String authorization, Long id) {
		final String method = "[ UserIntegrationService.verifyIfExist ] - ";
		LOGGER.debug(method + "BEGIN");

		RestTemplate restTemplate = new RestTemplate();
		final String url = environment.getProperty("cb.administration.service.url") + "users/" + id;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authorization);

		HttpEntity<User> request = new HttpEntity<>(null, headers);
		ResponseEntity<?> response;

		try {
			LOGGER.debug(method + "Calling the following URL: " + url);
			response = restTemplate.exchange(url, HttpMethod.GET, request, User.class);

			return (User) response.getBody();
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		} finally {
			LOGGER.debug(method + "END");
		}
	}

	/**
	 * Método responsável por buscar um usuário pelo username informado.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param authorization
	 * @param username
	 * @return
	 */
	public User findByUsername(String authorization, String username) {
		final String method = "[ UserIntegrationService.verifyIfExist ] - ";
		LOGGER.debug(method + "BEGIN");

		RestTemplate restTemplate = new RestTemplate();
		final String url = environment.getProperty("cb.administration.service.url") + "users";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authorization);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("username", username);

		HttpEntity<?> request = new HttpEntity<>(headers);
		ResponseEntity<?> response;

		try {
			LOGGER.debug(method + "Calling the following URL: " + url);
			response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, User.class);

			return (User) response.getBody();
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		} finally {
			LOGGER.debug(method + "END");
		}
	}

}
