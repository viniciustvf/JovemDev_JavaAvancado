package br.com.trier.springvespertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springvespertino.models.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer>{

	List<Team> findByNameStartingWithIgnoreCase(String name);
	
	Team findByName(String name);
	
	void findByName(Team team);
	
}
