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
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.services.TeamService;

@RestController
@RequestMapping("/team")
public class TeamResource {

	@Autowired
	private TeamService service;

	@Secured({ "ROLE_ADMIN" })
	@GetMapping("/{id}")
	public ResponseEntity<Team> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@Secured({ "ROLE_ADMIN" })
	@PostMapping
	public ResponseEntity<Team> insert(@RequestBody Team team) {
		return ResponseEntity.ok(service.insert(team));
	}

	@Secured({ "ROLE_ADMIN" })
	@PutMapping("/{id}")
	public ResponseEntity<Team> update(@PathVariable Integer id, @RequestBody Team team) {
		team.setId(id);
		return ResponseEntity.ok(service.update(team));
	}

	@Secured({ "ROLE_ADMIN" })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@Secured({ "ROLE_USER" })
	@GetMapping
	public ResponseEntity<List<Team>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	@Secured({ "ROLE_USER" })
	@GetMapping("/name-starting/{name}")
	public ResponseEntity<List<Team>> findByNameStartingWithIgnoreCase(@PathVariable String name) {
		return ResponseEntity.ok(service.findByNameStartingWithIgnoreCase(name));
	}

	@Secured({ "ROLE_USER" })
	@GetMapping("/name/{name}")
	public ResponseEntity<Team> findByNameIgnoreCase(@PathVariable String name) {
		return ResponseEntity.ok(service.findByNameIgnoreCase(name));
	}
}
