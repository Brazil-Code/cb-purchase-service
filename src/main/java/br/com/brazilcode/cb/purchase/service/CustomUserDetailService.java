package br.com.brazilcode.cb.purchase.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.brazilcode.cb.libs.model.User;
import br.com.brazilcode.cb.libs.repository.UserRepository;

/**
 * Classe de serviço para detalhes de usuário.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 6 de mar de 2020 12:19:21
 * @version 1.0
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public CustomUserDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = Optional.ofNullable(userRepository.findByUsername(username))
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");
		return new org.springframework.security.core.userdetails.User(user.getUsername(), "000", authorityListUser);
	}

}
