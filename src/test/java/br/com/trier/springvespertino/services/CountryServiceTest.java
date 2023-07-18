package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTests;
import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import jakarta.transaction.Transactional;

@Transactional
public class CountryServiceTest extends BaseTests {

	@Autowired
	CountryService countryService;

	@Test
	@DisplayName("Teste buscar pais por ID")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void findByIdTest() {
		var pais = countryService.findById(1);
		assertNotNull(pais);
		assertEquals(1, pais.getId());
		assertEquals("Pais 1", pais.getName());
	}

	@Test
	@DisplayName("Teste buscar pais por ID inexistente")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void findByIdWrongTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> countryService.findById(10));
		assertEquals("O pais 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar pais por nome")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void findByNameTest() {
		var pais = countryService.findByNameIgnoreCase("Pais 1");
		assertEquals("Pais 1", pais.getName());
		var exception = assertThrows(ObjectNotFound.class, () -> countryService.findByNameIgnoreCase("timi"));
		assertEquals("Nenhum pais timi cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar pais por letra que começa errada")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void findByNameStartingWithWrongTest() {
		var lista = countryService.findByNameStartingWithIgnoreCase("p");
		assertEquals(3, lista.size());
		var exception = assertThrows(ObjectNotFound.class, () -> countryService.findByNameStartingWithIgnoreCase("z"));
		assertEquals("Nenhum nome de pais inicia com z cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir pais")
	void insertCountryTest() {
		Country pais = new Country(null, "insert");
		countryService.insert(pais);
		pais = countryService.findById(pais.getId());
		assertEquals(1, pais.getId());
		assertEquals("insert", pais.getName());
	}

	@Test
	@DisplayName("Teste apagar pais")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void deleteByIdTest() {
		countryService.delete(1);
		List<Country> list = countryService.listAll();
		assertEquals(2, list.size());
	}

	@Test
	@DisplayName("Teste apagar pais inexistente")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void deleteByIdNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> countryService.delete(10));
		assertEquals("O pais 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste alterar pais inexistente")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void updateCountryNonExistsTest() {
		Country pais = new Country(20, "update");
		var exception = assertThrows(ObjectNotFound.class, () -> countryService.update(pais));
		assertEquals("O pais 20 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste listar todos sem pais cadastrados")
	void listAllNonExistsCountryTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> countryService.listAll());
		assertEquals("Nenhum pais cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir pais com nome duplicado")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void insertCountryPaisDuplicateTest() {
		Country pais = new Country(null, "Pais 1");
		var exception = assertThrows(IntegrityViolation.class, () -> countryService.insert(pais));
		assertEquals("Nome já existente: Pais 1", exception.getMessage());

	}

	@Test
	@DisplayName("Teste alterar pais com nome duplicado")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void updateCountryPaisWrongTest() {
		Country pais = new Country(2, "Pais 1");
		var exception = assertThrows(IntegrityViolation.class, () -> countryService.update(pais));
		assertEquals("Nome já existente: Pais 1", exception.getMessage());
	}

	@Test
	@DisplayName("Teste alterar pais")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void updateCountryTest() {
		Country pais = new Country(1, "update");
		countryService.update(pais);
		assertEquals(1, pais.getId());
		assertEquals("update", pais.getName());
	}

	@Test
	@DisplayName("Teste listar todos")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	void listAllTest() {
		List<Country> lista = countryService.listAll();
		assertEquals(3, lista.size());
	}
}