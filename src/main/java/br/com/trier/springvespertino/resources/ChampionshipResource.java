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

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.services.ChampionshipService;

@RestController
@RequestMapping("/championship")
public class ChampionshipResource {
	
	@Autowired
	private ChampionshipService service;
	
	@PostMapping
	public ResponseEntity<Championship> insert (@RequestBody Championship championship) {
		Championship newChampionship = service.insert(championship);
		return newChampionship != null ? ResponseEntity.ok(newChampionship) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Championship> findById (@PathVariable Integer id) {
		Championship Championship = service.findById(id);
		return Championship != null? ResponseEntity.ok(Championship) : ResponseEntity.badRequest().build();		
	}
	
	@GetMapping
	public ResponseEntity<List<Championship>> listAll () {
		List<Championship> Championship = service.listAll();
		return Championship.size() > 0 ? ResponseEntity.ok(Championship) : ResponseEntity.badRequest().build();		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Championship> update (@PathVariable Integer id, @RequestBody Championship Championship) {
		Championship.setId(id);
		Championship = service.update(Championship);
		return Championship != null ? ResponseEntity.ok(Championship) : ResponseEntity.badRequest().build();		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete (@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();		
	}
	
	@GetMapping("name/{description}")
	public ResponseEntity<List<Championship>> findByName (@PathVariable String description) {
		List<Championship> championship = service.findByDescription(description);
		return championship != null? ResponseEntity.ok(championship) : ResponseEntity.badRequest().build();		
	}
	
	@GetMapping("name-starting/{description}")
	public ResponseEntity<List<Championship>> findByDescriptionStartingWithIgnoreCase (@PathVariable String description) {
		List<Championship> championship = service.findByDescriptionStartingWithIgnoreCase(description);
		return championship != null? ResponseEntity.ok(championship) : ResponseEntity.badRequest().build();		
	}
	
	@GetMapping("year/{year}")
	public ResponseEntity<List<Championship>> findByYear (@PathVariable Integer year) {
		List<Championship> championship = service.findByYear(year);
		return championship != null? ResponseEntity.ok(championship) : ResponseEntity.badRequest().build();		
	}
	
	@GetMapping("year-between/{initialYear}/{finalYear}")
	public ResponseEntity<List<Championship>> findByYearBetween (@PathVariable Integer initialYear, @PathVariable Integer finalYear) {
		List<Championship> championship = service.findByYearBetween(initialYear, finalYear);
		return championship != null? ResponseEntity.ok(championship) : ResponseEntity.badRequest().build();		
	}
	
	
	
	
	
	
}
