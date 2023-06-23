package br.com.trier.springvespertino.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.repositories.CountryRepository;
import br.com.trier.springvespertino.services.CountryService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountryRepository repository;
	
	private void isCountryNameUnique(Country country) {
		Country busca = repository.findByNameIgnoreCase(country.getName());
		if ( busca != null && busca.getId() != country.getId()) {
			throw new IntegrityViolation("Nome já existente: %s".formatted(country.getName()));
		}
	}
	
	@Override
	public Country findById(Integer id) {
		Optional<Country> country = repository.findById(id);
		return country.orElseThrow(() -> new ObjectNotFound("O pais %s não existe".formatted(id)));
	}

	@Override
	public Country insert(Country country) {
		isCountryNameUnique(country);
		return repository.save(country);
	}

	@Override
	public Country update(Country country) {
		findById(country.getId());
		isCountryNameUnique(country);
		return repository.save(country);
	}
	
	@Override
	public void delete(Integer id) {
		Country country = findById(id);	
		if (country != null ) {
			repository.delete(country);
		}
	}
	
	@Override
	public List<Country> listAll() {
		List<Country> lista = repository.findAll();
		if ( lista.isEmpty() ) {
			throw new ObjectNotFound("Nenhum pais cadastrado");
		}
		return lista;
	}

	@Override
	public Country findByNameIgnoreCase(String name) {
		Country country = repository.findByNameIgnoreCase(name);
		if ( country == null) {
			throw new ObjectNotFound("Nenhum pais %s cadastrado".formatted(name));
		}
		return country;
	}

	@Override
	public List<Country> findByNameStartingWithIgnoreCase(String letra) {
		List<Country> lista = repository.findByNameStartingWithIgnoreCase(letra);
		if ( lista.isEmpty() ) {
			throw new ObjectNotFound("Nenhum nome de pais inicia com %s cadastrado".formatted(letra));
		}
		return lista;
	}
}
