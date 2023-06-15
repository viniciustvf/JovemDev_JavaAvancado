package br.com.trier.springvespertino.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.User;
import br.com.trier.springvespertino.repositories.UserRepository;
import br.com.trier.springvespertino.services.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository repository;
	
	@Override
	public User findById(Integer id) {
		Optional<User> user = repository.findById(id);
		return user.orElse(null);
	}

	@Override
	public User insert(User user) {
		return repository.save(user);
	}

	@Override
	public List<User> listAll() {
		return repository.findAll();
	}

	@Override
	public User update(User user) {
		return repository.save(user);
	}

	@Override
	public void delete(Integer id) {
		User user = findById(id);
		if ( user != null ) {
			repository.delete(user);
		}
	}
}
