package br.com.trier.springvespertino.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.models.dto.PilotDTO;
import br.com.trier.springvespertino.services.CountryService;
import br.com.trier.springvespertino.services.PilotService;
import br.com.trier.springvespertino.services.TeamService;
import br.com.trier.springvespertino.utils.DateUtils;

@RestController
@RequestMapping("/pilot")
public class PilotResource {

	@Autowired
	private PilotService service;
	
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private TeamService teamService;
	
	@GetMapping("/{id}")
	public ResponseEntity<PilotDTO> findById (@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id).toDTO());		  
	}
	
	@PostMapping
	public ResponseEntity<PilotDTO> insert (@RequestBody PilotDTO pilotDTO) {
		Pilot pilot = new Pilot(pilotDTO, countryService.findById(pilotDTO.getCountryId()), teamService.findById(pilotDTO.getTeamId()));
		return ResponseEntity.ok(service.insert(pilot).toDTO());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PilotDTO> update (@PathVariable Integer id, @RequestBody PilotDTO pilotDTO) {
		Pilot pilot = new Pilot(pilotDTO, countryService.findById(pilotDTO.getCountryId()), teamService.findById(pilotDTO.getTeamId()));
		pilot.setId(id);
		return ResponseEntity.ok(service.update(pilot).toDTO());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete (@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();		
	}	
	
	@GetMapping
	public ResponseEntity<List<PilotDTO>> listAll () {
		return ResponseEntity.ok(service.listAll().stream().map((pilot) -> pilot.toDTO()).toList());
	}
	  
	@GetMapping("/name/{name}")
	public ResponseEntity<List<PilotDTO>> findByName(@PathVariable String name) {
		List<Pilot> lista = service.findByName(name);
		return ResponseEntity.ok(lista.stream().map((pilot) -> pilot.toDTO()).toList());		
	} 
	
	@GetMapping("/name-starting/{name}")
	public ResponseEntity<List<PilotDTO>> findByNameStartingWithIgnoreCase(@PathVariable String name) {
		List<Pilot> lista = service.findByNameStartingWithIgnoreCase(name);
		return ResponseEntity.ok(lista.stream().map((pilot) -> pilot.toDTO()).toList());		
	}
	
	@GetMapping("/country/{countryId}")
	public ResponseEntity<List<PilotDTO>> findByCountry(@PathVariable Integer countryId) {
		List<Pilot> lista = service.findByCountry(countryService.findById(countryId));
		return ResponseEntity.ok(lista.stream().map((pilot) -> pilot.toDTO()).toList());		
	}
	
	@GetMapping("/team/{teamId}")
	public ResponseEntity<List<PilotDTO>> findByTeam(@PathVariable Integer teamId) {
		List<Pilot> lista = service.findByTeam(teamService.findById(teamId));
		return ResponseEntity.ok(lista.stream().map((pilot) -> pilot.toDTO()).toList());		
	}

}
