package br.com.trier.springvespertino.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode (of = "id")
@Entity(name="championship")
public class Championship {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_championship")
	private Integer id;
	
	@Column(name = "description_champ")
	private String description;
	
	@Column(name = "year_champ")
	private Integer year;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
