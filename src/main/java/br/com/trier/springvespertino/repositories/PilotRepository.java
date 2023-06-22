package br.com.trier.springvespertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.Team;

@Repository
public interface PilotRepository extends JpaRepository<Pilot, Integer>{
	
	List<Pilot> findByName(String name);
	
	List<Pilot> findByNameStartingWithIgnoreCase(String name);
	
	List<Pilot> findByCountry(Country country);
	
	List<Pilot> findByTeam(Team team);
	
}
