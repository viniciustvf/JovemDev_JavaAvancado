package br.com.trier.springvespertino.services.impl;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.Track;
import br.com.trier.springvespertino.repositories.RaceRepository;
import br.com.trier.springvespertino.services.RaceService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class RaceServiceImpl implements RaceService {

	@Autowired
	RaceRepository repository;
	
	private void validateRace(Race race) {
		if (!race.getChampionship().getYear().equals(race.getDate().getYear())) {
			throw new IntegrityViolation("A data da corrida deve corresponder ao ano do campeonato");
		}
	}
	
	@Override
	public Race findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("A corrida %s não existe".formatted(id)));
	}
 
	@Override
	public Race insert(Race race) {
		validateRace(race);
		return repository.save(race);
	}

	@Override
	public List<Race> listAll() {
		List<Race> lista = repository.findAll();
		if ( lista.isEmpty() ) {
			throw new ObjectNotFound("Nenhuma corrida cadastrada");
		}
		return lista;
	}

	@Override
	public Race update(Race race) {
		validateRace(race);
		return repository.save(race);
	}

	@Override
	public void delete(Integer id) {
		Race race = findById(id);
		repository.delete(race);
	}

	@Override
	public List<Race> findByDate(ZonedDateTime date) {
		List<Race> lista = repository.findByDate(date);
		if (lista.isEmpty()) {	
			throw new ObjectNotFound("Nenhuma corrida encontrada para a data %s".formatted(date));
		}
		return lista;
	}	

	@Override
	public List<Race> findByTrack(Track track) {
		List<Race> lista = repository.findByTrack(track);
		if (lista.isEmpty()) {	
			throw new ObjectNotFound("Nenhuma corrida encontrada para a pista %s".formatted(track));
		}
		return lista;
	}

	@Override
	public List<Race> findByTrackStartingWithIgnoreCaseOrderByDesc(Track track) {
		List<Race> lista = repository.findByTrack(track);
		if (lista.isEmpty()) {	
			throw new ObjectNotFound("Nenhuma corrida encontrada para a pista %s".formatted(track));
		}
		return lista;
	}

	@Override
	public List<Race> findByChampionship(Championship champ) {
		List<Race> lista = repository.findByChampionship(champ);
		if (lista.isEmpty()) {	
			throw new ObjectNotFound("Nenhuma corrida encontrada para o campeonato %s".formatted(champ));
		}
		return lista;
	}

	@Override
	public List<Race> findByChampionshipStartingWithIgnoreCaseOrderByDesc(Championship champ) {
		List<Race> lista = repository.findByChampionship(champ);
		if (lista.isEmpty()) {	
			throw new ObjectNotFound("Nenhuma corrida encontrada para o campeonato %s".formatted(champ));
		}
		return lista;
	}

	
}
