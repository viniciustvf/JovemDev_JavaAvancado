package br.com.trier.springvespertino.models;

import br.com.trier.springvespertino.models.dto.PilotRaceDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "pilot_race")
public class PilotRace {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pilotRace")
	private Integer id;

	@ManyToOne
	private Pilot pilot;

	@ManyToOne
	private Race race;

	@Column(name = "placing_pilotRace")
	private Integer placing;

	public PilotRace(PilotRaceDTO dto, Pilot pilot, Race race) {
		this(dto.getId(), pilot, race, dto.getPlacing());
	}

	public PilotRaceDTO toDTO() {
		return new PilotRaceDTO(getId(), pilot.getId(), pilot.getName(), race.getId(), getPlacing());
	}

}
