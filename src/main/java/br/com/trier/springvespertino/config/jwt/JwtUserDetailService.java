package br.com.trier.springvespertino.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.trier.springvespertino.repositories.UserRepository;


@Component
public class JwtUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		br.com.trier.springvespertino.models.User user = repository.findByEmail(email).orElseThrow(null);
		return User.builder()
				.username(user.getEmail())
				.password(encoder.encode(user.getPassword()))
				.roles(user.getRoles().split(","))
				.build();
	}
	

}