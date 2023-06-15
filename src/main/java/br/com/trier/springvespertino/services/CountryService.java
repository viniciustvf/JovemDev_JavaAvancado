package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Country;

public interface CountryService {

	Country findById(Integer id);
	
	Country insert(Country country);
	
	List<Country> listAll();
	
	Country update (Country country);
	
	void delete (Integer id);
}
