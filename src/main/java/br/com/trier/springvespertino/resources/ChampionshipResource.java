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
		return ResponseEntity.ok(service.insert(championship));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Championship> findById (@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));		  
	}
	
	@GetMapping
	public ResponseEntity<List<Championship>> listAll () {
		return ResponseEntity.ok(service.listAll());	
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Championship> update (@PathVariable Integer id, @RequestBody Championship championship) {
		championship.setId(id);
		return ResponseEntity.ok(service.update(championship));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete (@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();		
	}	
	
	@GetMapping("/description/{description}")
	public ResponseEntity<List<Championship>> findByNameIgnoreCase(@PathVariable String description) {
		return ResponseEntity.ok(service.findByDescriptionIgnoreCase(description));		
	}
	
	@GetMapping("description-starting/{description}")
	public ResponseEntity<List<Championship>> findByDescriptionStartingWithIgnoreCase (@PathVariable String description) {
		return ResponseEntity.ok(service.findByDescriptionStartingWithIgnoreCase(description));		
	}
	
	@GetMapping("year/{year}")
	public ResponseEntity<List<Championship>> findByYear (@PathVariable Integer year) {
		return ResponseEntity.ok(service.findByYear(year));		
	}
	
	@GetMapping("year-between/{initialYear}/{finalYear}")
	public ResponseEntity<List<Championship>> findByYearBetween (@PathVariable Integer initialYear, @PathVariable Integer finalYear) {
			return ResponseEntity.ok(service.findByYearBetween(initialYear, finalYear));
		}
	
}
