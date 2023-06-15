package br.com.trier.springvespertino.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Dado {

	private List<Integer> numeros;
	private Integer soma;
	private Double percentualSorteado;
	
}
