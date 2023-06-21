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
	
	@GetMapping("/{id}")
	public ResponseEntity<Track> findById (@PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));		  
	}
	
	@PostMapping
	public ResponseEntity<Track> insert (@RequestBody Track track) {
		countryService.findById(track.getCountry().getId());
		return ResponseEntity.ok(service.insert(track));
	}
	
	@GetMapping
	public ResponseEntity<List<Track>> listAll () {
		return ResponseEntity.ok(service.listAll());	
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Track> update (@PathVariable Integer id, @RequestBody Track track) {
		track.setId(id);
		return ResponseEntity.ok(service.update(track));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete (@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();		
	}	
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Track>> findByNameStartingWithIgnoreCase(@PathVariable String name){
		return ResponseEntity.ok(service.findByNameStartingWithIgnoreCase(name));
	}
	
	@GetMapping("/size/{sizeIn}/{sizeFin}")
	public ResponseEntity<List<Track>> findBySizeBetween(Integer sizeIn, Integer sizeFin){
		return ResponseEntity.ok(service.findBySizeBetween(sizeIn, sizeFin));
	}
	
	@GetMapping("/country/{idCountry}")
	public ResponseEntity<List<Track>> findByCountryOrderBySizeDesc(@PathVariable Integer idCountry){
		return ResponseEntity.ok(service.findByCountryOrderBySizeDesc(countryService.findById(idCountry)));
	}

}











