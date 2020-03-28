package br.com.brazilcode.cb.purchase.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Classe responsável por disponibilizar métodos utilitários para HTTP Requests.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 28 de mar de 2020 15:32:52
 * @version 1.0
 */
@Service
public class HttpRequestUtils {

	/**
	 * Método responsável por buscar os dados que estão sendo trafegados na requisição atual.
	 *
	 * @author Brazil Code - Gabriel Guarido
	 * @return
	 */
	public HttpServletRequest getCurrentRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
		Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
		HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
		Assert.state(servletRequest != null, "Could not find current HttpServletRequest");

		return servletRequest;
	}

}
