package br.com.trier.springvespertino.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.repositories.ChampionshipRepository;
import br.com.trier.springvespertino.services.ChampionshipService;

public class ChampionshipServiceImpl implements ChampionshipService {

	@Autowired
	private ChampionshipRepository repository;
	
	@Override
	public Championship findById(Integer id) {
		Optional<Championship> championship = repository.findById(id);
		return championship.orElse(null);
	}

	@Override
	public Championship insert(Championship championship) {
		return repository.save(championship);
	}

	@Override
	public List<Championship> listAll() {
		return repository.findAll(); 
	}

	@Override
	public Championship update(Championship championship) {
		return repository.save(championship);
	}

	@Override
	public void delete(Integer id) {
		Championship championship = findById(id);
		if (championship != null) {
			repository.delete(championship);
		}
	}
}
