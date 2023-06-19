package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Championship;

public interface ChampionshipService {

	Championship findById(Integer id);
	
	Championship insert(Championship championship);
	
	List<Championship> listAll();
	
	Championship update (Championship championship);
	
	void delete (Integer id);
	
	List<Championship> findByDescriptionStartingWithIgnoreCase(String name);
	
	List<Championship> findByDescription(String name);
	
	List<Championship>findByYear(Integer year);
	
	List<Championship>findByYearBetween(Integer initialYear, Integer finallYear);
	
}
