package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Team;

public interface CountryService {

	Country findById(Integer id);
	
	Country insert(Country country);
	
	List<Country> listAll();
	
	Country update (Country country);
	
	void delete (Integer id);
	
	List<Team> findByNameStartingWithIgnoreCase(String name);
	
	List<Team> findByName(String name);
	
}
