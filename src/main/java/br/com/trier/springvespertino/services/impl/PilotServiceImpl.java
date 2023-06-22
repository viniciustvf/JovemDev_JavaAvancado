package br.com.trier.springvespertino.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.repositories.PilotRepository;
import br.com.trier.springvespertino.services.PilotService;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class PilotServiceImpl implements PilotService {

	@Autowired
	PilotRepository repository;
	
	@Override
	public Pilot findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("A piloto %s não existe".formatted(id)));
	}
 
	@Override
	public Pilot insert(Pilot pilot) {
		findById(pilot.getId());
		return repository.save(pilot);
	}

	@Override
	public List<Pilot> listAll() {
		List<Pilot> lista = repository.findAll();
		if ( lista.isEmpty() ) {
			throw new ObjectNotFound("Nenhum piloto cadastrado");
		}
		return lista;
	}

	@Override
	public Pilot update(Pilot pilot) {
		findById(pilot.getId());
		return repository.save(pilot);
	}

	@Override
	public void delete(Integer id) {
		Pilot pilot = findById(id);
		repository.delete(pilot);
	}

	@Override
	public List<Pilot> findByName(String name) {
		if (name.isEmpty()) {
			throw new ObjectNotFound("O nome não pode ser nulo");
		}
		return repository.findByName(name);
	}

	@Override
	public List<Pilot> findByNameStartingWithIgnoreCase(String name) {
		if (name.isEmpty()) {
			throw new ObjectNotFound("O nome não pode ser nulo");
		}
		return repository.findByNameStartingWithIgnoreCase(name);
	}

	@Override
	public List<Pilot> findByCountry(Country country) {
		List<Pilot> lista = repository.findByCountry(country);
		if (lista.isEmpty()) {	
			throw new ObjectNotFound("Nenhum piloto encontrado para o país %s".formatted(country));
		}
		return lista;
	}

	@Override
	public List<Pilot> findByTeam(Team team) {
		List<Pilot> lista = repository.findByTeam(team);
		if (lista.isEmpty()) {	
			throw new ObjectNotFound("Nenhum piloto encontrado para o time %s".formatted(team));
		}
		return lista;
	}
}
