package br.com.trier.springvespertino.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.models.User;
import br.com.trier.springvespertino.repositories.TeamRepository;
import br.com.trier.springvespertino.services.TeamService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamRepository repository;

	private void isTeamNameUnique(Team team) {
		Team busca = repository.findByNameIgnoreCase(team.getName());
		if (busca != null && busca.getId() != team.getId()) {
			throw new IntegrityViolation("Nome já existente: %s".formatted(team.getName()));
		}
	}

	@Override
	public Team findById(Integer id) {
		Optional<Team> team = repository.findById(id);
		return team.orElseThrow(() -> new ObjectNotFound("O time %s não existe".formatted(id)));
	}

	@Override
	public Team insert(Team team) {
		isTeamNameUnique(team);
		return repository.save(team);
	}

	@Override
	public Team update(Team team) {
		findById(team.getId());
		isTeamNameUnique(team);
		return repository.save(team);
	}

	@Override
	public void delete(Integer id) {
		Team team = findById(id);
		if (team != null) {
			repository.delete(team);
		}
	}

	@Override
	public List<Team> listAll() {
		List<Team> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum time cadastrado");
		}
		return lista;
	}

	@Override
	public Team findByNameIgnoreCase(String name) {
		Team team = repository.findByNameIgnoreCase(name);
		if (team == null) {
			throw new ObjectNotFound("Nenhum time %s cadastrado".formatted(name));
		}
		return team;
	}

	@Override
	public List<Team> findByNameStartingWithIgnoreCase(String letra) {
		List<Team> lista = repository.findByNameStartingWithIgnoreCase(letra);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum nome de time inicia com %s cadastrado".formatted(letra));
		}
		return lista;
	}
}
