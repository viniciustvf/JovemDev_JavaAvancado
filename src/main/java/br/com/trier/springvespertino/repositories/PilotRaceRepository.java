package br.com.trier.springvespertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.PilotRace;
import br.com.trier.springvespertino.models.Race;

@Repository
public interface PilotRaceRepository extends JpaRepository<PilotRace, Integer> {

	List<PilotRace> findByPlacing(Integer placing);

	List<PilotRace> findByPlacingBetween(Integer initialPlacing, Integer finallPlacing);

	List<PilotRace> findByPilot(Pilot pilot);

	List<PilotRace> findByRace(Race race);

}
