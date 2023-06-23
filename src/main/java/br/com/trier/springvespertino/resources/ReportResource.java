package br.com.trier.springvespertino.resources;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.PilotRace;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.dto.PilotPodiumYearDTO;
import br.com.trier.springvespertino.models.dto.PilotRaceDTO;
import br.com.trier.springvespertino.models.dto.RaceCountryYearDTO;
import br.com.trier.springvespertino.models.dto.RaceDTO;
import br.com.trier.springvespertino.services.CountryService;
import br.com.trier.springvespertino.services.PilotRaceService;
import br.com.trier.springvespertino.services.PilotService;
import br.com.trier.springvespertino.services.RaceService;
import br.com.trier.springvespertino.services.TrackService;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@RestController
@RequestMapping("/reports")
public class ReportResource {

	@Autowired
	CountryService countryService;
	
	@Autowired
	RaceService raceService;
	
	@Autowired
	TrackService trackService;
	
	@Autowired
	PilotRaceService pilotRaceService;
	
	@Autowired
	PilotService pilotService;
	
	//Todas as corridas que ocorreram em um país em um determinado ano
	
	@GetMapping("/races-by-country-year/{countryId}/{year}")
	public ResponseEntity<RaceCountryYearDTO> findRaceByCountryAndYear(@PathVariable Integer countryId, @PathVariable Integer year){
		Country country = countryService.findById(countryId);
		List<RaceDTO> raceDTOs = trackService.findByCountryOrderBySizeDesc(country).stream()
		        .flatMap(track -> {
		            try {
		                return raceService.findByTrack(track).stream();
		            } catch (ObjectNotFound e) {
		                return Stream.empty();
		            }
		        })
		        .filter(race -> race.getDate().getYear() == year)
		        .map(Race::toDTO)
		        .toList();

		return ResponseEntity.ok(new RaceCountryYearDTO(year, country.getName(), raceDTOs.size(), raceDTOs));
	}
	
	//Todos os pilotos que ficaram no pódio em um determinado ano
	
	@GetMapping("/pilots-podium-by-year/{year}")
	public ResponseEntity<PilotPodiumYearDTO> findPilotsPodiumByYear(@PathVariable Integer year) {
	    List<Race> races = raceService.findByDateContainsYear(year);
	    List<PilotRaceDTO> pilotRaceDTOsSelected = races.stream()
	            .flatMap(race -> {
	                try {
	                    return pilotRaceService.findByRace(race).stream()
	                            .filter(pilotRace -> pilotRace.getPlacing() <= 3)
	                            .map(PilotRace::toDTO);
	                } catch (ObjectNotFound e) {
	                    return Stream.empty();
	                }
	            })
	            .collect(Collectors.toList());

	    return ResponseEntity.ok(new PilotPodiumYearDTO(year, pilotRaceDTOsSelected.size(), pilotRaceDTOsSelected));
	}
}
