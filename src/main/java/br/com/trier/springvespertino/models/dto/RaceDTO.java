package br.com.trier.springvespertino.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RaceDTO {

	private Integer id;

	private String date;

	private Integer trackId;

	private String trackName;

	private Integer championshipId;

	private String championshipDescription;

}
