package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.Team;

public interface PilotService {

	Pilot findById(Integer id);

	Pilot insert(Pilot team);

	List<Pilot> listAll();

	Pilot update (Pilot team);

	void delete (Integer id);
	
	List<Pilot> findByName(String name);
	
	List<Pilot> findByNameStartingWithIgnoreCase(String name);
	
	List<Pilot> findByCountry(Country country);
	
	List<Pilot> findByCountryStatingWithIgnoreCase(Country country);
	
	List<Pilot> findByTeam(Team team);
	
	List<Pilot> findByTeamStatingWithIgnoreCase(Team team);

}