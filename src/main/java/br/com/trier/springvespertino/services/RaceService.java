package br.com.trier.springvespertino.services;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.Track;

public interface RaceService {

	Race findById(Integer id);

	Race insert(Race race);

	List<Race> listAll();

	Race update (Race race);

	void delete (Integer id);
	
	List<Race> findByDate(ZonedDateTime date);
	
	List<Race> findByTrack(Track track);
	
	List<Race> findByTrackStartingWithIgnoreCaseOrderByDesc(Track track);
	
	List<Race> findByChampionship(Championship champ);
	
	List<Race> findByChampionshipStartingWithIgnoreCaseOrderByDesc(Championship champ);
}