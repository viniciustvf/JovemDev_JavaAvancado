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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.SpringVespertinoApplication;
import br.com.trier.springvespertino.models.dto.RaceCountryYearDTO;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = SpringVespertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportResourceTest {

	@Autowired
	protected TestRestTemplate rest;
	
	@Test
	@DisplayName("Teste buscar corridas por país em um determinado ano")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
	@Sql({"classpath:/resources/sqls/piloto_corrida.sql"})
	public void findRaceByYearAndCountryTest() {
	    ResponseEntity<RaceCountryYearDTO> response = rest.getForEntity("/races-by-country-year/{country}/{year}", RaceCountryYearDTO.class, 1, 2020);
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    ResponseEntity<RaceCountryYearDTO> errorResponse = rest.getForEntity("/races-by-country-year/{country}/{year}", RaceCountryYearDTO.class, 10, 2022);
	    assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
	}
	

	
}