package br.com.brazilcode.cb.purchase.service.integration.administration;

import java.io.Serializable;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.brazilcode.cb.libs.dto.LogDTO;
import br.com.brazilcode.cb.libs.model.User;
import br.com.brazilcode.cb.purchase.constants.SecurityConstants;
import br.com.brazilcode.cb.purchase.exception.integration.LogIntegrationServiceException;
import br.com.brazilcode.cb.purchase.exception.integration.UserIntegrationServiceException;

/**
 * Classe responsável por fazer a integração via REST com o serviço de Logs do módulo Administration.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 18 de mar de 2020 23:54:31
 * @version 1.0
 */
@Service
public class LogIntegrationService implements Serializable {

	private static final long serialVersionUID = 4004907005178097217L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LogIntegrationService.class);

	@Autowired
	private UserIntegrationService userIntegrationService;

	@Value("${cb.administration.service.url}")
	private String administrationServiceURL;

	/**
	 * Método responsável por fazer uma chamada via REST para o serviço de gravação de Logs.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @param description
	 * @throws LogIntegrationServiceException
	 * @throws UserIntegrationServiceException
	 */
	public void createLog(String description) throws LogIntegrationServiceException, UserIntegrationServiceException {
		final String method = "[ LogIntegrationService.createLog ] - ";
		LOGGER.debug(method + "BEGIN");

		RestTemplate restTemplate = new RestTemplate();
		final String url = administrationServiceURL + "logs";

		String authorization = getCurrentRequest().getHeader(SecurityConstants.HEADER_STRING);
		String username = getCurrentRequest().getUserPrincipal().getName();

		LOGGER.debug(method + "Calling userIntegrationService.findByUsername");
		User user = this.userIntegrationService.findByUsername(authorization, username);
		LogDTO logDTO = new LogDTO(user.getId(), description, String.valueOf(Calendar.getInstance()));

		LOGGER.debug(method + "Setting HTTP Headers");
		HttpHeaders headers = new HttpHeaders();
		headers.set(SecurityConstants.HEADER_STRING, authorization);

		LOGGER.debug(method + "Building request");
		HttpEntity<LogDTO> request = new HttpEntity<>(logDTO, headers);

		try {
			LOGGER.debug(method + "Calling the following URL: " + url);
			restTemplate.postForEntity(url, request, null);
		} catch (Exception e) {
			LOGGER.error(method + e.getMessage(), e);
			throw new LogIntegrationServiceException(e.getMessage(), e);
		} finally {
			LOGGER.debug(method + "END");
		}
	}

	/**
	 * Método responsável por buscar os dados que estão sendo trafegados na requisição atual.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @return
	 */
	private static HttpServletRequest getCurrentRequest() {
		final String method = "[ LogIntegrationService.getCurrentRequest ] - ";
		LOGGER.debug(method + "BEGIN");

		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
		Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
		HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
		Assert.state(servletRequest != null, "Could not find current HttpServletRequest");

		LOGGER.debug(method + "END");
		return servletRequest;
	}

}
