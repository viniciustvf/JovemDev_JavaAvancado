package br.com.trier.springvespertino.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.User;
import br.com.trier.springvespertino.repositories.UserRepository;
import br.com.trier.springvespertino.services.UserService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository repository;
	
	private void isEmailUnique(User user) {
		Optional<User> busca = repository.findByEmail(user.getEmail()); 
		if ( busca.isPresent() && busca.get().getId() != user.getId()) {
			throw new IntegrityViolation("Email já existente: %s".formatted(user.getEmail()));
		}
	}
 
	@Override
	public User findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("O usuário %s não existe".formatted(id)));
	}

	@Override
	public User insert(User user) {
		isEmailUnique(user);
		return repository.save(user);
	}

	@Override
	public User update(User user) {
		findById(user.getId());
		isEmailUnique(user);
		return repository.save(user);
	}
	
	@Override
	public void delete(Integer id) {
		User user = findById(id);
		repository.delete(user);
	}
	
	@Override
	public List<User> listAll() {
		List<User> lista = repository.findAll();
		if ( lista.isEmpty() ) {
			throw new ObjectNotFound("Nenhum usuário cadastrado");
		}
		return lista;
	}

	@Override
	public List<User> findByNameStartingWithIgnoreCase(String letra) {
		List<User> lista = repository.findByNameStartingWithIgnoreCase(letra);
		if ( lista.isEmpty() ) {
			throw new ObjectNotFound("Nenhum nome de usuário inicia com %s cadastrado".formatted(letra));
		}
		return lista;
	}

	@Override
	public List<User> findByNameIgnoreCase(String name) {
		List<User> lista = repository.findByNameIgnoreCase(name);
		if ( lista.isEmpty() ) {
			throw new ObjectNotFound("Nenhum nome de usuário %s cadastrado".formatted(name));
		}
		return lista;
	}

	@Override
	public Optional<User> findByEmail(String email) {
	    Optional<User> user = repository.findByEmail(email);
		if(user.isPresent()) {
			return user;
		} else {
			throw new ObjectNotFound("Nenhum email de usuário %s cadastrado".formatted(email));
		}
	}

	@Override
	public Optional<User> findByName(String name) {
		Optional<User> user = repository.findByName(name);
		if(user.isPresent()) {
			return user;
		} else {
			throw new ObjectNotFound("Nenhum nome de usuário %s cadastrado".formatted(name));
		}
	}
}
