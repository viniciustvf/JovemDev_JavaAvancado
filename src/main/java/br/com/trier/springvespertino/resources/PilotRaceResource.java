package br.com.trier.springvespertino.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.PilotRace;
import br.com.trier.springvespertino.models.dto.PilotRaceDTO;
import br.com.trier.springvespertino.services.PilotRaceService;
import br.com.trier.springvespertino.services.PilotService;
import br.com.trier.springvespertino.services.RaceService;

@RestController
@RequestMapping("/pilotRace")
public class PilotRaceResource {

	@Autowired
	private PilotRaceService service;
	
	@Autowired
	private PilotService pilotService;
	
	@Autowired
	private RaceService raceService;
	
	@GetMapping("/{id}")
	public ResponseEntity<PilotRaceDTO> findById (@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id).toDTO());		  
	}
	
	@PostMapping
	public ResponseEntity<PilotRaceDTO> insert (@RequestBody PilotRaceDTO pilotRaceDTO) {
		PilotRace pilotRace = new PilotRace(pilotRaceDTO, pilotService.findById(pilotRaceDTO.getPilotId()), raceService.findById(pilotRaceDTO.getRaceId()));
		return ResponseEntity.ok(service.insert(pilotRace).toDTO());
	}

	@PutMapping("/{id}")
	public ResponseEntity<PilotRaceDTO> update (@PathVariable Integer id, @RequestBody PilotRaceDTO pilotRaceDTO) {
		PilotRace pilotRace = new PilotRace(pilotRaceDTO, pilotService.findById(pilotRaceDTO.getPilotId()), raceService.findById(pilotRaceDTO.getRaceId()));
		pilotRace.setId(id);
		return ResponseEntity.ok(service.update(pilotRace).toDTO());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete (@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();		
	}	
	
	@GetMapping
	public ResponseEntity<List<PilotRaceDTO>> listAll () {
		return ResponseEntity.ok(service.listAll().stream().map((pilotRace) -> pilotRace.toDTO()).toList());
	}
	
	@GetMapping("/placing/{placing}")
	public ResponseEntity<List<PilotRaceDTO>> findByPlacing(@PathVariable Integer placing) {
		List<PilotRace> lista = service.findByPlacing(placing);
		return ResponseEntity.ok(lista.stream().map((pilotRace) -> pilotRace.toDTO()).toList());		
	} 
	
	@GetMapping("/placing-between/{initialPlacing}/{finalPlacing}")
	public ResponseEntity<List<PilotRaceDTO>> findByPlacingBetween(@PathVariable Integer initialPlacing, @PathVariable Integer finalPlacing) {
		List<PilotRace> lista = service.findByPlacingBetween(initialPlacing, finalPlacing);
		return ResponseEntity.ok(lista.stream().map((pilotRace) -> pilotRace.toDTO()).toList());		
	} 
	
	@GetMapping("/pilot/{pilotId}")
	public ResponseEntity<List<PilotRaceDTO>> findByPilot(@PathVariable Integer pilotId) {
		List<PilotRace> lista = service.findByPilot(pilotService.findById(pilotId));
		return ResponseEntity.ok(lista.stream().map((pilotRace) -> pilotRace.toDTO()).toList());		
	} 
	
	@GetMapping("/race/{raceId}")
	public ResponseEntity<List<PilotRaceDTO>> findByRace(@PathVariable Integer raceId) {
		List<PilotRace> lista = service.findByRace(raceService.findById(raceId));
		return ResponseEntity.ok(lista.stream().map((pilotRace) -> pilotRace.toDTO()).toList());		
	} 

}
