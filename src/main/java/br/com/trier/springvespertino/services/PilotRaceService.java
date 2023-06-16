package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.PilotRace;

public interface PilotRaceService {

	PilotRace findById(Integer id);
	
	PilotRace insert(PilotRace pilotRace);
	
	List<PilotRace> listAll();
	
	PilotRace update (PilotRace pilotRace);
	
	void delete (Integer id);
}
