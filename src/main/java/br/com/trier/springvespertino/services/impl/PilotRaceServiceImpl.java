package br.com.trier.springvespertino.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.PilotRace;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.repositories.PilotRaceRepository;
import br.com.trier.springvespertino.services.PilotRaceService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class PilotRaceServiceImpl implements PilotRaceService{

	@Autowired
	PilotRaceRepository repository;
	
	private void validatePilotRace(PilotRace pilotRace) {
		if(pilotRace.getPilot() == null) {
			throw new IntegrityViolation("O piloto não pode ser nulo");
		}
		if(pilotRace.getRace() == null) {
			throw new IntegrityViolation("A corrida não pode ser nula");
		}
		if(pilotRace.getPlacing() == null) {
			throw new IntegrityViolation("A colocação não pode ser nula");
		}
	}
	
	@Override
	public PilotRace findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("A corrida piloto %s não existe".formatted(id)));
	}

	@Override
	public PilotRace insert(PilotRace pilotRace) {
		validatePilotRace(pilotRace);
		return repository.save(pilotRace);
	}

	@Override
	public PilotRace update(PilotRace pilotRace) {
		findById(pilotRace.getId());
		validatePilotRace(pilotRace);
		return repository.save(pilotRace);
	}
	
	@Override
	public void delete(Integer id) {
		PilotRace pilotRace = findById(id);
		repository.delete(pilotRace);
	}
	
	@Override
	public List<PilotRace> listAll() {
		List<PilotRace> lista = repository.findAll();
		if ( lista.isEmpty() ) {
			throw new ObjectNotFound("Nenhum piloto corrida cadastrado");
		}
		return lista;
	}

	@Override
	public List<PilotRace> findByPlacing(Integer placing) {
		List<PilotRace> lista = repository.findByPlacing(placing);
		if (lista.isEmpty()) {	
			throw new ObjectNotFound("Nenhuma corrida piloto encontrada para a colocação %s".formatted(placing));
		}
		return lista;
	}

	@Override
	public List<PilotRace> findByPlacingBetween(Integer initialPlacing, Integer finalPlacing) {
		List<PilotRace> lista = repository.findByPlacingBetween(initialPlacing, finalPlacing);
		if (lista.isEmpty()) {	
			throw new ObjectNotFound("Nenhuma corrida piloto encontrada entre a colocação %s° e %s°".formatted(initialPlacing, finalPlacing));
		}
		return lista;
	}

	@Override
	public List<PilotRace> findByPilot(Pilot pilot) {
		List<PilotRace> lista = repository.findByPilot(pilot);
		if (lista.isEmpty()) {	
			throw new ObjectNotFound("Nenhuma corrida piloto encontrada para o piloto %s".formatted(pilot.getName()));
		}
		return lista;
	}

	@Override
	public List<PilotRace> findByRace(Race race) {
		List<PilotRace> lista = repository.findByRace(race);
		if (lista.isEmpty()) {	
			throw new ObjectNotFound("Nenhuma corrida piloto encontrada para a corrida %s".formatted(race.getId()));
		}
		return lista;
	}
}
