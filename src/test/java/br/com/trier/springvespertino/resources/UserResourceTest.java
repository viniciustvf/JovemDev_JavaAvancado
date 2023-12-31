package br.com.trier.springvespertino.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import br.com.trier.springvespertino.models.dto.UserDTO;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@SpringBootTest(classes = SpringVespertinoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest {

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
	
	
	private ResponseEntity<UserDTO> getUser(String url) {
		return rest.exchange(url, 
				HttpMethod.GET, 
				new HttpEntity<>(getHeaders("Email 1", "Senha 1")), 
				UserDTO.class
			);
	}

	private ResponseEntity<List<UserDTO>> getUsers(String url) {
	    return rest.exchange(
	        url,
	        HttpMethod.GET,
	        new HttpEntity<>(getHeaders("Email 1", "Senha 1")),
	        new ParameterizedTypeReference<List<UserDTO>>() {}
	    );
	}
	
	@Test
	@DisplayName("Inserir usuário")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql({"classpath:/resources/sqls/usuario.sql"})
	public void insertUserTest() {
		UserDTO dto = new UserDTO(null, "nome", "vinizin@hotmail.com", "senha", "ADMIN,USER");
		HttpHeaders headers = getHeaders("Email 1", "Senha 1");
		HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<UserDTO> responseEntity = rest.exchange(
	            "/user", 
	            HttpMethod.POST,  
	            requestEntity,    
	            UserDTO.class   
	    );
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		UserDTO user = responseEntity.getBody();
		assertEquals("nome", user.getName());
	}
	
	@Test
	@DisplayName("Buscar por id")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/usuario.sql")
	public void findByIdTest() {
		ResponseEntity<UserDTO> response = getUser("/user/3");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		UserDTO user = response.getBody();
		assertEquals("User 1", user.getName());
	}

	@Test
	@DisplayName("Buscar por id inexistente")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/usuario.sql")
	public void findByIdNonExistsTest() {
		ResponseEntity<UserDTO> response = getUser("/user/100");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Teste listar todos os usuários")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/usuario.sql")
	public void listAllTest() {
		ResponseEntity<List<UserDTO>> response = getUsers("/user"); 
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@DisplayName("Teste alterar usuário")
	public void updateUserTest() {
		UserDTO dto = new UserDTO(null, "update", "update", "update", "ADMIN,USER");
		HttpHeaders headers = getHeaders("Email 1", "Senha 1");
		HttpEntity<UserDTO> requestEntity = new HttpEntity<>(dto, headers);
		ResponseEntity<UserDTO> responseEntity = rest.exchange(
	            "/user/3", 
	            HttpMethod.PUT,  
	            requestEntity,    
	            UserDTO.class   
	    );
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		UserDTO user = responseEntity.getBody();
		assertEquals("update", user.getName());
		assertEquals(3, user.getId());
	}
	
	@Test
	@DisplayName("Teste deletar usuário")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/usuario.sql")
	public void deleteUserTest() {
		HttpHeaders headers = getHeaders("Email 1", "Senha 1");
		HttpEntity<UserDTO> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Void> responseEntity = rest.exchange(
	            "/user/3", 
	            HttpMethod.DELETE,  
	            requestEntity,
	            Void.class   
	    );		
		assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Buscar por nome")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/usuario.sql")
	public void findByNameTest() {
		ResponseEntity<UserDTO> response = getUser("/user/name/User 1");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		UserDTO user = response.getBody();
		assertEquals("User 1", user.getName());

		ResponseEntity<UserDTO> response2 = getUser("/user/name/uzer1");
	    assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Buscar por letra que inicia ignore case")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/usuario.sql")
	public void findByNameStartingTest() {
		ResponseEntity<List<UserDTO>> response = getUsers("/user/name-starting/u");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		List<UserDTO> user = response.getBody();
		assertEquals(2, user.size());

		ResponseEntity<UserDTO> response2 = getUser("/user/name-starting/z");
	    assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Buscar por nome ignore case")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/usuario.sql")
	public void findByNameIgnoreCaseTest() {
		ResponseEntity<List<UserDTO>> response = getUsers("/user/name-ignore-case/user 1");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		List<UserDTO> user = response.getBody();
		assertEquals(1, user.size());

		ResponseEntity<UserDTO> response2 = getUser("/user/name/uzer 1");
	    assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	@DisplayName("Buscar por email")
	@Sql("classpath:/resources/sqls/limpa_tabelas.sql")
	@Sql("classpath:/resources/sqls/usuario.sql")
	public void findByEmailTest() {
		ResponseEntity<UserDTO> response = getUser("/user/email/Email 1");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		UserDTO user = response.getBody();
		assertEquals("Email 1", user.getEmail());

		ResponseEntity<UserDTO> response2 = getUser("/user/email/email 1");
	    assertEquals(response2.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
}