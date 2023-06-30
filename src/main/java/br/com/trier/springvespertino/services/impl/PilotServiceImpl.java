package br.com.trier.springvespertino.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.repositories.PilotRepository;
import br.com.trier.springvespertino.services.PilotService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class PilotServiceImpl implements PilotService {

	@Autowired
	PilotRepository repository;

	private void validatePilot(Pilot pilot) {
		if (pilot.getCountry() == null) {
			throw new IntegrityViolation("O país não pode ser nulo");
		}
		if (pilot.getTeam() == null) {
			throw new IntegrityViolation("O time não pode ser nulo");
		}
	}

	@Override
	public Pilot findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("O piloto %s não existe".formatted(id)));
	}

	@Override
	public Pilot insert(Pilot pilot) {
		validatePilot(pilot);
		return repository.save(pilot);
	}

	@Override
	public List<Pilot> listAll() {
		List<Pilot> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum piloto cadastrado");
		}
		return lista;
	}

	@Override
	public Pilot update(Pilot pilot) {
		findById(pilot.getId());
		validatePilot(pilot);
		return repository.save(pilot);
	}

	@Override
	public void delete(Integer id) {
		Pilot pilot = findById(id);
		repository.delete(pilot);
	}

	@Override
	public List<Pilot> findByName(String name) {
		List<Pilot> lista = repository.findByName(name);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum nome %s encontrado".formatted(name));
		}
		return lista;
	}

	@Override
	public List<Pilot> findByNameStartingWithIgnoreCase(String name) {
		List<Pilot> lista = repository.findByNameStartingWithIgnoreCase(name);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum nome %s encontrado".formatted(name));
		}
		return lista;
	}

	@Override
	public List<Pilot> findByCountry(Country country) {
		List<Pilot> lista = repository.findByCountry(country);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum piloto encontrado para o país %s".formatted(country.getName()));
		}
		return lista;
	}

	@Override
	public List<Pilot> findByTeam(Team team) {
		List<Pilot> lista = repository.findByTeam(team);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhum piloto encontrado para o time %s".formatted(team.getName()));
		}
		return lista;
	}
}
