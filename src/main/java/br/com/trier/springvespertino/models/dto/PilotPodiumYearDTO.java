package br.com.trier.springvespertino.models.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PilotPodiumYearDTO {

	private Integer year;

	private Integer pilotSize;

	private List<PilotRaceDTO> pilotRaceDTO;

}
