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

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.services.CountryService;

@RestController
@RequestMapping("/country")
public class CountryResource {

	@Autowired
	private CountryService service;

	// @Secured({ "ROLE_USER" })
	@GetMapping("/{id}")
	public ResponseEntity<Country> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	// @Secured({ "ROLE_ADMIN" })
	@PostMapping
	public ResponseEntity<Country> insert(@RequestBody Country country) {
		return ResponseEntity.ok(service.insert(country));
	}

	// @Secured({ "ROLE_ADMIN" })
	@PutMapping("/{id}")
	public ResponseEntity<Country> update(@PathVariable Integer id, @RequestBody Country country) {
		country.setId(id);
		return ResponseEntity.ok(service.update(country));
	}

	// @Secured({ "ROLE_ADMIN" })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	// @Secured({ "ROLE_USER" })
	@GetMapping
	public ResponseEntity<List<Country>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	// @Secured({ "ROLE_USER" })
	@GetMapping("/name-starting/{name}")
	public ResponseEntity<List<Country>> findByNameStartingWithIgnoreCase(@PathVariable String name) {
		return ResponseEntity.ok(service.findByNameStartingWithIgnoreCase(name));
	}

	// @Secured({ "ROLE_USER" })
	@GetMapping("/name/{name}")
	public ResponseEntity<Country> findByNameIgnoreCase(@PathVariable String name) {
		return ResponseEntity.ok(service.findByNameIgnoreCase(name));
	}
}
