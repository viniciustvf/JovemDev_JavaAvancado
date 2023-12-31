package br.com.trier.springvespertino.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.dto.RaceDTO;
import br.com.trier.springvespertino.services.ChampionshipService;
import br.com.trier.springvespertino.services.RaceService;
import br.com.trier.springvespertino.services.TrackService;
import br.com.trier.springvespertino.utils.DateUtils;

@RestController
@RequestMapping("/race")
public class RaceResource {

	@Autowired
	private RaceService service;

	@Autowired
	private TrackService trackService;

	@Autowired
	private ChampionshipService championshipService;

	@Secured({ "ROLE_USER" })
	@GetMapping("/{id}")
	public ResponseEntity<RaceDTO> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id).toDTO());
	}

	@Secured({ "ROLE_ADMIN" })
	@PostMapping
	public ResponseEntity<RaceDTO> insert(@RequestBody RaceDTO raceDTO) {
		Race race = new Race(raceDTO, championshipService.findById(raceDTO.getChampionshipId()),
				trackService.findById(raceDTO.getTrackId()));
		return ResponseEntity.ok(service.insert(race).toDTO());
	}

	@Secured({ "ROLE_ADMIN" })
	@PutMapping("/{id}")
	public ResponseEntity<RaceDTO> update(@PathVariable Integer id, @RequestBody RaceDTO raceDTO) {
		Race race = new Race(raceDTO, championshipService.findById(raceDTO.getId()),
				trackService.findById(raceDTO.getId()));
		race.setId(id);
		return ResponseEntity.ok(service.update(race).toDTO());
	}

	@Secured({ "ROLE_ADMIN" })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Secured({ "ROLE_USER" })
	@GetMapping
	public ResponseEntity<List<RaceDTO>> listAll() {
		return ResponseEntity.ok(service.listAll().stream().map((race) -> race.toDTO()).toList());
	}

	@Secured({ "ROLE_USER" })
	@GetMapping("/date")
	public ResponseEntity<List<RaceDTO>> findByDate(@RequestParam String date) {
		List<Race> lista = service.findByDate(DateUtils.strToZonedDateTime(date));
		return ResponseEntity.ok(lista.stream().map((race) -> race.toDTO()).toList());
	}

	@Secured({ "ROLE_USER" })
	@GetMapping("/track/{trackId}")
	public ResponseEntity<List<RaceDTO>> findByTrack(@PathVariable Integer trackId) {
		List<Race> lista = service.findByTrack(trackService.findById(trackId));
		return ResponseEntity.ok(lista.stream().map((race) -> race.toDTO()).toList());
	}

	@Secured({ "ROLE_USER" })
	@GetMapping("/championship/{championshipId}")
	public ResponseEntity<List<RaceDTO>> findByChampionship(@PathVariable Integer championshipId) {
		List<Race> lista = service.findByChampionship(championshipService.findById(championshipId));
		return ResponseEntity.ok(lista.stream().map((race) -> race.toDTO()).toList());
	}

}
