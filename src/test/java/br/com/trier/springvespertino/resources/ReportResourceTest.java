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
import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.dto.RaceCountryYearDTO;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = SpringVespertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportResourceTest {

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
	
	@Test
	@DisplayName("Teste buscar corridas por país em um determinado ano")
	@Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"})
	@Sql({"classpath:/resources/sqls/piloto_corrida.sql"})
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void findRaceByYearAndCountryTest() {
		HttpHeaders headers = getHeaders("Email 1", "Senha 1");
		HttpEntity<RaceCountryYearDTO> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<RaceCountryYearDTO> response = rest.exchange(
	            "/reports/races-by-country-year/1/2020",
	            HttpMethod.GET,
	            requestEntity,
	            new ParameterizedTypeReference<RaceCountryYearDTO>() {}
	        );
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    RaceCountryYearDTO race = response.getBody();
	    assertEquals(2020, race.getYear());
	    
	}
	
	@Test
	@DisplayName("Teste buscar pilotos que podiaram em um determinado ano")
	@Sql({"classpath:/resources/sqls/limpa_tabelas.sql"})
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"})
	@Sql({"classpath:/resources/sqls/piloto_corrida.sql"})
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void findPilotsPodiumByYearTest() {
		HttpHeaders headers = getHeaders("Email 1", "Senha 1");
		HttpEntity<RaceCountryYearDTO> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<RaceCountryYearDTO> response = rest.exchange(
	            "/reports/pilots-podium-by-year/2020",
	            HttpMethod.GET,
	            requestEntity,
	            new ParameterizedTypeReference<RaceCountryYearDTO>() {}
	        );
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    RaceCountryYearDTO pilot = response.getBody();
	    assertEquals(2020, pilot.getYear());
	    
	}
	
}