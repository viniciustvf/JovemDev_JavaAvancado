package br.com.trier.springvespertino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springvespertino.models.PilotRace;

@Repository
public interface PilotRaceRepository extends JpaRepository<PilotRace, Integer>{

}
