package br.com.trier.springvespertino.models;

import br.com.trier.springvespertino.models.dto.UserDTO;
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
@Entity(name="app_user")
public class User {
	
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user")
	private Integer id;
	
	@Column(name = "name_user")
	private String name;
	
	@Column(name = "email_user", unique = true)
	private String email;
	
	@Column(name = "password_user")
	private String password;
	
	public User (UserDTO dto) {
		this(dto.getId(), dto.getName(), dto.getEmail(), dto.getPassword()); 
	}
	
	public UserDTO toDTO() {
		return new UserDTO(id, name, email, password); 
	}

}
