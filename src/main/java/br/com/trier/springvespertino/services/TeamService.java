package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Team;

public interface TeamService {

	Team findById(Integer id);

	Team insert(Team team);

	List<Team> listAll();

	Team update(Team team);

	void delete(Integer id);

	Team findByNameIgnoreCase(String name);

	List<Team> findByNameStartingWithIgnoreCase(String name);

}
