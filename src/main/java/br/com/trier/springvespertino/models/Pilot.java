package br.com.trier.springvespertino.models;

import br.com.trier.springvespertino.models.dto.PilotDTO;
import br.com.trier.springvespertino.models.dto.RaceDTO;
import br.com.trier.springvespertino.utils.DateUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Entity(name = "pilot")
public class Pilot {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_pilot")
	private Integer id;

	@Column(name = "name_pilot")
	private String name;

	@ManyToOne
	private Country country;

	@ManyToOne
	private Team team;

	public Pilot(PilotDTO dto, Country country, Team team) {
		this(dto.getId(), dto.getName(), country, team);
	}

	public PilotDTO toDTO() {
		return new PilotDTO(getId(), getName(), country.getId(), country.getName(), team.getId(), team.getName());
	}

}