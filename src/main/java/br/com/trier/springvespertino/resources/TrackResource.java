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

import br.com.trier.springvespertino.models.Track;
import br.com.trier.springvespertino.services.CountryService;
import br.com.trier.springvespertino.services.TrackService;

@RestController
@RequestMapping("/track")
public class TrackResource {

	@Autowired
	TrackService service;

	@Autowired
	CountryService countryService;

	 @Secured({ "ROLE_USER" })
	@GetMapping("/{id}")
	public ResponseEntity<Track> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	 @Secured({ "ROLE_ADMIN" })
	@PostMapping
	public ResponseEntity<Track> insert(@RequestBody Track track) {
		countryService.findById(track.getCountry().getId());
		return ResponseEntity.ok(service.insert(track));
	}

	 @Secured({ "ROLE_ADMIN" })
	@PutMapping("/{id}")
	public ResponseEntity<Track> update(@PathVariable Integer id, @RequestBody Track track) {
		track.setId(id);
		return ResponseEntity.ok(service.update(track));
	}

	 @Secured({ "ROLE_ADMIN" })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	 @Secured({ "ROLE_USER" })
	@GetMapping
	public ResponseEntity<List<Track>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	 @Secured({ "ROLE_USER" })
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Track>> findByNameStartingWithIgnoreCase(@PathVariable String name) {
		return ResponseEntity.ok(service.findByNameStartingWithIgnoreCase(name));
	}

	 @Secured({ "ROLE_USER" })
	@GetMapping("/size/{sizeIn}/{sizeFin}")
	public ResponseEntity<List<Track>> findBySizeBetween(@PathVariable Integer sizeIn, @PathVariable Integer sizeFin) {
		return ResponseEntity.ok(service.findBySizeBetween(sizeIn, sizeFin));
	}

	 @Secured({ "ROLE_USER" })
	@GetMapping("/country/{idCountry}")
	public ResponseEntity<List<Track>> findByCountryOrderBySizeDesc(@PathVariable Integer idCountry) {
		return ResponseEntity.ok(service.findByCountryOrderBySizeDesc(countryService.findById(idCountry)));
	}
}
