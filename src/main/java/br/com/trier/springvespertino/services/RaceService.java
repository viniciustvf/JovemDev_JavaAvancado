package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Race;

public interface RaceService {

	Race findById(Integer id);
	
	Race insert(Race race);
	
	List<Race> listAll();
	
	Race update (Race race);
	
	void delete (Integer id);
}
