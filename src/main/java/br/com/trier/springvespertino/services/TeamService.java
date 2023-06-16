package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.models.User;

public interface TeamService {

	Team findById(Integer id);
	
	Team insert(Team team);
	
	List<Team> listAll();
	
	Team update (Team team);
	
	void delete (Integer id);
	
	List<Team> findByName(String name);
	
	List<Team> findByNameStartingWithIgnoreCase(String name);
	
}
