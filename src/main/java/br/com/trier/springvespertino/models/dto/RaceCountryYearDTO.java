package br.com.trier.springvespertino.models.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RaceCountryYearDTO {

	private Integer year;

	private String country;

	private Integer raceSize;

	private List<RaceDTO> racers;

}
