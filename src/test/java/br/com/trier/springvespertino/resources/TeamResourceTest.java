package br.com.trier.springvespertino.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.SpringVespertinoApplication;
import br.com.trier.springvespertino.config.jwt.LoginDTO;
import br.com.trier.springvespertino.models.Team;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = SpringVespertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamResourceTest {

	@Autowired
	protected TestRestTemplate rest;

	private HttpHeaders  getHeaders(String email, String senha) {
		LoginDTO loginDTO = new LoginDTO(email, senha);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(loginDTO, headers);
	    ResponseEntity<String> responseEntity = rest.exchange(
	            "/auth/token",
	            HttpMethod.POST,
	            requestEntity,
	            String.class
	    );
	    assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	    HttpHeaders headersRet = new HttpHeaders();
	    headersRet.setBearerAuth(responseEntity.getBody());
	    return headersRet;
	}
	
	
	private ResponseEntity<Team> getTeam(String url) {
		return rest.exchange(url, 
				HttpMethod.GET, 
				new HttpEntity<>(getHeaders("Email 1", "Senha 1")), 
				Team.class
			);
	}

	private ResponseEntity<List<Team>> getTeams(String url) {
	    return rest.exchange(
	        url,
	        HttpMethod.GET,
	        new HttpEntity<>(getHeaders("Email 1", "Senha 1")),
	        new ParameterizedTypeReference<List<Team>>() {}
	    );
	}
	
	@Test
	@DisplayName("Inserir time")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void insertTeamTest() {
		Team dto = new Team(null, "time");
		HttpHeaders headers = getHeaders("Email 1", "Senha 1");
		HttpEntity<Team> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<Team> responseEntity = rest.exchange(
	            "/team", 
	            HttpMethod.POST,  
	            requestEntity,    
	            Team.class   
	    );
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Team team2 = responseEntity.getBody();
		assertEquals("time", team2.getName());
	}
	
	@Test
	@DisplayName("Buscar por id")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/time.sql")
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void findByIdTest() {
		ResponseEntity<Team> response = getTeam("/team/1");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Team team = response.getBody();
		assertEquals("Time 1", team.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/time.sql")
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void findByIdNonExistsTest() {
		ResponseEntity<Team> response = getTeam("/team/100");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Teste listar todos os times")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/time.sql")
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void listAllTest() {
		ResponseEntity<List<Team>> response = getTeams("/team"); 
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@DisplayName("Teste alterar time")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/time.sql")
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void updateTeamTest() {
		Team dto = new Team(null, "update");
		HttpHeaders headers = getHeaders("Email 1", "Senha 1");
		HttpEntity<Team> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<Team> responseEntity = rest.exchange(
	            "/team/1", 
	            HttpMethod.PUT,  
	            requestEntity,    
	            Team.class   
	    );
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		Team team = responseEntity.getBody();
		assertEquals("update", team.getName());
		assertEquals(1, team.getId());
	}
	
	@Test
	@DisplayName("Teste deletar time")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/time.sql")
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void deleteTeamTest() {
		HttpHeaders headers = getHeaders("Email 1", "Senha 1");
		HttpEntity<Team> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Void> responseEntity = rest.exchange(
	            "/team/1", 
	            HttpMethod.DELETE,  
	            requestEntity,
	            Void.class   
	    );		
		assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Buscar por letra que inicia ignore case")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/time.sql")
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void findByNameStartingTest() {
		ResponseEntity<List<Team>> response = getTeams("/team/name-starting/t");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		List<Team> team = response.getBody();
		assertEquals(3, team.size());

		ResponseEntity<Team> response2 = getTeam("/team/name-starting/z");
	    assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Buscar por nome ignore case")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/time.sql")
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void findByNameIgnoreCaseTest() {
		ResponseEntity<Team> response = getTeam("/team/name/time 1");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Team user = response.getBody();
		assertEquals("Time 1", user.getName());

		ResponseEntity<Team> response2 = getTeam("/team/name/timi 1");
	    assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
}