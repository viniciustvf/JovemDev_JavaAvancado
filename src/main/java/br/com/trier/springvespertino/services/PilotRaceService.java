package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.PilotRace;
import br.com.trier.springvespertino.models.Race;

public interface PilotRaceService {

	PilotRace findById(Integer id);

	PilotRace insert(PilotRace pilotRace);

	List<PilotRace> listAll();

	PilotRace update (PilotRace pilotRace);

	void delete (Integer id);
	
	List<PilotRace> findByPlacing(Integer placing);
	
	List<PilotRace>findByPlacingBetween(Integer initialPlacing, Integer finallPlacing);
	
	List<PilotRace> findByPilot(Pilot pilot);
	
	List<PilotRace> findByRace(Race race);
}
