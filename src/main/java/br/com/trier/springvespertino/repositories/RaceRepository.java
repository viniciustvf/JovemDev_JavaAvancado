package br.com.trier.springvespertino.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.Track;

@Repository
public interface RaceRepository extends JpaRepository<Race, Integer>{

	@Query(value = "SELECT * FROM race r WHERE EXTRACT(YEAR FROM r.date_race) = :year", nativeQuery = true)
	List<Race> findByDateContainsYear(@Param("year") Integer year);
	
	List<Race> findByDate(ZonedDateTime date);
	
	List<Race> findByTrack(Track track);
	
	List<Race> findByChampionship(Championship champ);
	
}