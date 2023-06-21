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
import br.com.trier.springvespertino.models.Country;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = SpringVespertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CountryResourceTest {

	@Autowired
	protected TestRestTemplate rest;

	private ResponseEntity<Country> getCountry(String url) {
		return rest.getForEntity(url, Country.class);
	}

	private ResponseEntity<List<Country>> getCountrys(String url) {
	    return rest.exchange(
	        url,
	        HttpMethod.GET,
	        null,
	        new ParameterizedTypeReference<List<Country>>() {}
	    );
	}
	
	@Test
	@DisplayName("Inserir pais")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	public void insertCountryTest() {
		Country dto = new Country(null, "pais");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Country> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<Country> responseEntity = rest.exchange(
	            "/country", 
	            HttpMethod.POST,  
	            requestEntity,    
	            Country.class   
	    );
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		Country country = responseEntity.getBody();
		assertEquals("pais", country.getName());
	}
	
	@Test
	@DisplayName("Buscar por id")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/pais.sql")
	public void findByIdTest() {
		ResponseEntity<Country> response = getCountry("/country/1");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Country country = response.getBody();
		assertEquals("Pais 1", country.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/pais.sql")
	public void findByIdNonExistsTest() {
		ResponseEntity<Country> response = getCountry("/country/100");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Teste listar todos os paiss")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/pais.sql")
	public void listAllTest() {
		ResponseEntity<List<Country>> response = getCountrys("/country"); 
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@DisplayName("Teste alterar pais")
	public void updateCountryTest() {
		Country dto = new Country(null, "update");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Country> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<Country> responseEntity = rest.exchange(
	            "/country/1", 
	            HttpMethod.PUT,  
	            requestEntity,    
	            Country.class   
	    );
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		Country country = responseEntity.getBody();
		assertEquals("update", country.getName());
		assertEquals(1, country.getId());
	}
	
	@Test
	@DisplayName("Teste deletar pais")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/pais.sql")
	public void deleteCountryTest() {
		ResponseEntity<Void> responseEntity = rest.exchange(
	            "/country/1", 
	            HttpMethod.DELETE,  
	            null,
	            Void.class   
	    );		
		assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Buscar por letra que inicia ignore case")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/pais.sql")
	public void findByNameStartingTest() {
		ResponseEntity<List<Country>> response = getCountrys("/country/name-starting/p");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		List<Country> country = response.getBody();
		assertEquals(2, country.size());

		ResponseEntity<Country> response2 = getCountry("/country/name-starting/z");
	    assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Buscar por nome ignore case")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/pais.sql")
	public void findByNameIgnoreCaseTest() {
		ResponseEntity<Country> response = getCountry("/country/name/pais 1");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		Country user = response.getBody();
		assertEquals("Pais 1", user.getName());

		ResponseEntity<Country> response2 = getCountry("/country/name/timi 1");
	    assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
}