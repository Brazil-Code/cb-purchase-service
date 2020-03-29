package br.com.brazilcode.cb.purchase.service.integration.administration;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.brazilcode.cb.libs.exception.ResourceNotFoundException;
import br.com.brazilcode.cb.libs.model.User;
import br.com.brazilcode.cb.purchase.constants.SecurityConstants;
import br.com.brazilcode.cb.purchase.exception.integration.UserIntegrationServiceException;
import br.com.brazilcode.cb.purchase.utils.HttpRequestUtils;

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

	@Value("${cb.administration.service.url}")
	private String administrationServiceURL;

	@Autowired
	private HttpRequestUtils httpRequestUtils;

	/**
	 * Método responsável por fazer uma chamada via REST para o serviço de busca de Users do módulo Administration.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param id
	 * @return
	 * @throws UserIntegrationServiceException
	 * @throws RestClientException - Caso ocorra algum erro durante a comunicação com a API
	 * @throws ResourceNotFoundException - Caso o ID informado não exista na base de dados
	 */
	public User verifyIfExists(Long id) throws UserIntegrationServiceException {
		final String method = "[ UserIntegrationService.verifyIfExist ] - ";
		LOGGER.debug(method + "BEGIN");

		RestTemplate restTemplate = new RestTemplate();
		final String url = administrationServiceURL + "users/" + id;

		String authorization = this.httpRequestUtils.getCurrentRequest().getHeader(SecurityConstants.HEADER_STRING);

		LOGGER.debug(method + "Setting HEADERS to the request");
		HttpHeaders headers = new HttpHeaders();
		headers.set(SecurityConstants.HEADER_STRING, authorization);

		LOGGER.debug(method + "Building the request");
		HttpEntity<User> request = new HttpEntity<>(null, headers);
		ResponseEntity<?> response;

		try {
			LOGGER.debug(method + "Calling the following URL: " + url);
			response = restTemplate.exchange(url, HttpMethod.GET, request, User.class);

			return (User) response.getBody();
		} catch (HttpClientErrorException.NotFound e) {
			LOGGER.error(method + e.getMessage(), e);
			throw new UserIntegrationServiceException(e.getMessage(), e);
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
	 * @throws UserIntegrationServiceException
	 */
	public User findByUsername(String username) throws UserIntegrationServiceException {
		final String method = "[ UserIntegrationService.verifyIfExist ] - ";
		LOGGER.debug(method + "BEGIN");

		RestTemplate restTemplate = new RestTemplate();
		final String url = administrationServiceURL + "users";

		String authorization = this.httpRequestUtils.getCurrentRequest().getHeader(SecurityConstants.HEADER_STRING);

		HttpHeaders headers = new HttpHeaders();
		headers.set(SecurityConstants.HEADER_STRING, authorization);

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("username", username);

		LOGGER.debug(method + "Building the request");
		HttpEntity<?> request = new HttpEntity<>(headers);
		ResponseEntity<?> response;

		try {
			LOGGER.debug(method + "Calling the following URL: " + url);
			response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, User.class);

			return (User) response.getBody();
		} catch (HttpClientErrorException.NotFound e) {
			LOGGER.error(method + e.getMessage(), e);
			throw new UserIntegrationServiceException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw e;
		} finally {
			LOGGER.debug(method + "END");
		}
	}

}
