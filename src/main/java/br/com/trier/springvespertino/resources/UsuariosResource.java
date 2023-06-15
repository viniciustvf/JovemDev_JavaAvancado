package br.com.trier.springvespertino.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.User;

@RestController
@RequestMapping("/usuarios")
public class UsuariosResource {

	List<User> lista = new ArrayList<User>();
	
	@GetMapping
	public ResponseEntity<List<User>> listAll(){
		return ResponseEntity.ok(lista);
	}
	
	@PostMapping
	public ResponseEntity<User> add(@RequestBody User u){
		u.setId(lista.size() + 1);
		lista.add(u);
		return ResponseEntity.ok(u);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable Integer id) {
		User u = lista.stream().filter(user -> user.getId().equals(id))
				    .findAny()
				    .orElse(null);
		return u != null? ResponseEntity.ok(u): ResponseEntity.noContent().build();
	}
	
	
	
	
	
	
	
	
}
