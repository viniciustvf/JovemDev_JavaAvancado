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

import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.services.TeamService;

@RestController
@RequestMapping("/team")
public class TeamResource {
	
	@Autowired
	private TeamService service;
	
	@PostMapping
	public ResponseEntity<Team> insert (@RequestBody Team team) {
		Team newTeam = service.insert(team);
		return newTeam != null ? ResponseEntity.ok(newTeam) : ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Team> findById (@PathVariable Integer id) {
		Team team = service.findById(id);
		return team != null? ResponseEntity.ok(team) : ResponseEntity.badRequest().build();		
	}
	
	@GetMapping
	public ResponseEntity<List<Team>> listAll () {
		List<Team> team = service.listAll();
		return team.size() > 0 ? ResponseEntity.ok(team) : ResponseEntity.badRequest().build();		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Team> update (@PathVariable Integer id, @RequestBody Team team) {
		team.setId(id);
		team = service.update(team);
		return team != null ? ResponseEntity.ok(team) : ResponseEntity.badRequest().build();		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete (@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.ok().build();		
	}
}
