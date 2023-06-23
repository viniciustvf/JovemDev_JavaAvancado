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
import br.com.trier.springvespertino.models.Championship;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = SpringVespertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChampionshipResourceTest {

	@Autowired
	protected TestRestTemplate rest;

	private ResponseEntity<Championship> getChampionship(String url) {
		return rest.getForEntity(url, Championship.class);
	}

	private ResponseEntity<List<Championship>> getChampionships(String url) {
	    return rest.exchange(
	        url,
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<List<Championship>>() {}
	    );
	}
	
	@Test
	@DisplayName("Inserir campeonato")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	public void insertChampionshipTest() {
		Championship dto = new Championship(null, "campeonato", 1990);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Championship> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<Championship> responseEntity = rest.exchange(
	            "/championship", 
	            HttpMethod.POST,  
	            requestEntity,    
	            Championship.class   
	    );
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Championship championship = responseEntity.getBody();
		assertEquals("campeonato", championship.getDescription());
	}
	
	@Test
	@DisplayName("Inserir campeonato com ano errado")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	public void insertChampionshipWithWrongYearTest() {
		Championship dto = new Championship(null, "campeonato", 1989);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Championship> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<Championship> responseEntity = rest.exchange(
	            "/championship", 
	            HttpMethod.POST,  
	            requestEntity,    
	            Championship.class   
	    );
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
	
	@Test
	@DisplayName("Buscar por id")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/campeonato.sql")
	public void findByIdTest() {
		ResponseEntity<Championship> response = getChampionship("/championship/1");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Championship championship = response.getBody();
		assertEquals("Campeonato 1", championship.getDescription());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/campeonato.sql")
	public void findByIdNonExistsTest() {
		ResponseEntity<Championship> response = getChampionship("/championship/100");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Teste listar todos os campeonatos")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/campeonato.sql")
	public void listAllTest() {
		ResponseEntity<List<Championship>> response = getChampionships("/championship"); 
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@DisplayName("Teste listar todos os campeonatos sem campeonatos")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	public void listAllWrongTest() {
		ResponseEntity<Championship> response = getChampionship("/championship"); 
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Teste alterar campeonato")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/campeonato.sql")
	public void updateChampionshipTest() {
		Championship dto = new Championship(null, "update", 2010);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Championship> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<Championship> responseEntity = rest.exchange(
	            "/championship/1", 
	            HttpMethod.PUT,  
	            requestEntity,    
	            Championship.class   
	    );
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		Championship championship = responseEntity.getBody();
		assertEquals("update", championship.getDescription()); 
		assertEquals(2010, championship.getYear());
		assertEquals(1, championship.getId());
	}
	
	@Test
	@DisplayName("Teste deletar campeonato")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/campeonato.sql")
	public void deleteChampionshipTest() {
		ResponseEntity<Void> responseEntity = rest.exchange(
	            "/championship/1", 
	            HttpMethod.DELETE,  
	            null,
	            Void.class   
	    );		
		assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Buscar por letra que inicia ignore case")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/campeonato.sql")
	public void findByDescriptionStartingTest() {
		ResponseEntity<List<Championship>> response = getChampionships("/championship/description-starting/c");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		List<Championship> championship = response.getBody();
		assertEquals(3, championship.size());

		ResponseEntity<Championship> response2 = getChampionship("/championship/description-starting/z");
	    assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Buscar por nome ignore case")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/campeonato.sql")
	public void findByDescriptionIgnoreCaseTest() {
		ResponseEntity<List<Championship>> response = getChampionships("/championship/description/campeonato 1");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		List<Championship> champ = response.getBody();
		assertEquals(1, champ.size());

		ResponseEntity<Championship> response2 = getChampionship("/championship/description/campionato 1");
	    assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Buscar por ano")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/campeonato.sql")
	public void findByYearTest() {
		ResponseEntity<List<Championship>> response = getChampionships("/championship/year/2020");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		List<Championship> champ = response.getBody();
		assertEquals(1, champ.size());

		ResponseEntity<Championship> response2 = getChampionship("/championship/description/2013");
	    assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND); 
	}
	
	@Test
	@DisplayName("Buscar por ano between")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/campeonato.sql")
	public void findByYearBetweenTest() {
		ResponseEntity<List<Championship>> response = getChampionships("/championship/year-between/2015/2024");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		List<Championship> champ = response.getBody();
		assertEquals(2, champ.size());

		ResponseEntity<Championship> response2 = getChampionship("/championship/year-between/2013/2025");
	    assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());  
	}
	
	
}