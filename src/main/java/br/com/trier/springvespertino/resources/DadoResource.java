package br.com.trier.springvespertino.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.Dado;

@RestController
@RequestMapping("/dados")
public class DadoResource {

	List<Dado> list = new ArrayList<Dado>();
	
	@PostMapping("/lancar/{qt}/{aposta}")
	public ResponseEntity<List<Dado>> lancarDados( @PathVariable Integer qt, @PathVariable Integer aposta) {
		
		Integer n1 = 0;
		
		if (qt > 4 || qt < 1) {
			return ResponseEntity.badRequest().body(null);
		} 
		
		if (aposta < 1 || aposta > (qt * 6)) {
			return ResponseEntity.badRequest().body(null);
		}

		List<Integer> numerosDados = new ArrayList<>();
		Integer somaTotal = 0;
		
		for(int i = 0 ; i < qt ; i++) {
			n1 =+ (int) (Math.random() * 6) + 1;
			numerosDados.add(n1);
			somaTotal += n1;
		}

		Double percentualSorteado = (double)(somaTotal * 100) / aposta;
		
		list.add(new Dado(numerosDados, somaTotal, percentualSorteado));
		
		return ResponseEntity.ok(list);
	}

	@GetMapping("/buscar-por-soma/{soma}")
	public ResponseEntity<List<Dado>> findBySoma(@PathVariable Integer soma) {
		List<Dado> d = list.stream()
			.filter(dado -> dado.getSoma().equals(soma))
			.collect(Collectors.toList());
		
		return d.isEmpty()? ResponseEntity.badRequest().build() : ResponseEntity.ok(d);

	}
	
}
