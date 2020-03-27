package br.com.brazilcode.cb.purchase.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Classe responsável por disponibilizar métodos de parse de objetos.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 26 de mar de 2020 21:26:17
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class MapperUtils {

	@Autowired
	private Mapper mapper;

	public <T> T parse(Object object, Class<T> target) {
		return mapper.map(object, target);
	}

	public <T> Optional<T> parseOptional(Object object, Class<T> target) {
		return Optional.ofNullable(parse(object, target));
	}

	public <T> List<T> parseList(List<?> listObject, Class<T> target) {
		List<T> listRetorno = new ArrayList<T>();
		for (Object object : listObject) {
			T objectTarget = mapper.map(object, target);
			listRetorno.add(objectTarget);
		}
		return listRetorno;
	}

	public <T> Optional<List<T>> parseListOptional(List<?> listObject, Class<T> target) {
		return Optional.ofNullable(parseList(listObject, target));
	}

	public <T> T parseLazy(Object object, Class<T> target) {
		try {
			final T instance = target.newInstance();
			BeanUtils.copyProperties(instance, object);
			return instance;
		} catch (Exception e) {
			return null;
		}
	}

	public <T> Optional<T> parseLazyOptional(Object object, Class<T> target) {
		return Optional.ofNullable(parseLazy(object, target));
	}

}
