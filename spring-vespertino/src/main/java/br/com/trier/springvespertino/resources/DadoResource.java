package br.com.trier.springvespertino.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.trier.springvespertino.models.Dado;

@RestController
@RequestMapping("/dados")
public class DadoResource {

	@PostMapping("/lancar")
	public ResponseEntity<Dado> lancarDados( @RequestParam Integer qt, @RequestParam Integer aposta) {
		
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
		
		Dado resultado = new Dado(numerosDados, somaTotal, percentualSorteado);
		return ResponseEntity.ok(resultado);
	}
	
}
