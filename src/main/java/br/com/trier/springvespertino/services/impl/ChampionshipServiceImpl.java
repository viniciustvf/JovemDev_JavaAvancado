package br.com.trier.springvespertino.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.repositories.ChampionshipRepository;
import br.com.trier.springvespertino.services.ChampionshipService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class ChampionshipServiceImpl implements ChampionshipService {

	@Autowired
	private ChampionshipRepository repository;
	
	private void validateYearChampionship(Championship championship) {
		LocalDate validDate = LocalDate.now().plusYears(1);
		if(championship.getYear() > validDate.getYear() || championship.getYear() < 1990) {
			throw new IntegrityViolation("Ano deve ser maior que 1990 e menor que %s".formatted(validDate.getYear()));
		}
	}
	
	@Override
	public Championship findById(Integer id) {
		Optional<Championship> championship = repository.findById(id);
		return championship.orElseThrow(() -> new ObjectNotFound("O campeonato %s não existe".formatted(id)));
	}

	@Override
	public Championship insert(Championship championship) {
		validateYearChampionship(championship);
		return repository.save(championship);
	}

	@Override
	public List<Championship> listAll() {
		List<Championship> lista = repository.findAll();
		if ( lista.isEmpty() ) {
			throw new ObjectNotFound("Nenhum campeonato cadastrado");
		}
		return lista;
	}

	@Override
	public Championship update(Championship championship) {
		findById(championship.getId());
		validateYearChampionship(championship);
		return repository.save(championship);
	}

	@Override
	public void delete(Integer id) {
		Championship championship = findById(id);	
		if (championship != null ) {
			repository.delete(championship);
		}
	}

	@Override
	public List<Championship> findByDescriptionIgnoreCase(String description) {
		List<Championship> championship = repository.findByDescriptionIgnoreCase(description);
		if ( championship.isEmpty() ) {
			throw new ObjectNotFound("Nenhum campeonato %s cadastrado".formatted(description));
		}
		return championship;
	}

	@Override
	public List<Championship> findByDescriptionStartingWithIgnoreCase(String letra) {
		List<Championship> lista = repository.findByDescriptionStartingWithIgnoreCase(letra);
		if ( lista.isEmpty() ) {
			throw new ObjectNotFound("Nenhum campeonato inicia com %s cadastrado".formatted(letra));
		}
		return lista;
	}

	@Override
	public List<Championship> findByYear(Integer year) {
		List<Championship> championship = repository.findByYear(year); 
		if ( championship.isEmpty() ) {
			throw new ObjectNotFound("Nenhum campeonato em %s cadastrado".formatted(year));
		}
		return championship;
	}

	@Override
	public List<Championship> findByYearBetween(Integer initialYear, Integer finallYear) {
		List<Championship> championship = repository.findByYearBetween(initialYear, finallYear);
		if ( championship.isEmpty() ) {
			throw new ObjectNotFound("Nenhum campeonato entre %s e %s cadastrado".formatted(initialYear, finallYear));
		}
		return championship;
	}
	
}
