package br.com.trier.springvespertino.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import br.com.trier.springvespertino.models.dto.RaceDTO;
import br.com.trier.springvespertino.utils.DateUtils;
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
@Entity(name = "race")
public class Race {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_race")
	private Integer id;

	@Column(name = "date_race")
	private ZonedDateTime date;

	@ManyToOne
	private Track track;

	@ManyToOne
	private Championship championship;

	public Race(RaceDTO dto, Championship championship, Track track) {
		this(dto.getId(), DateUtils.strToZonedDateTime(dto.getDate()), track, championship);
	}

	public RaceDTO toDTO() {
		return new RaceDTO(id, DateUtils.zonedDateTimeToStr(date), track.getId(), track.getName(), championship.getId(),
				championship.getDescription());
	}
}