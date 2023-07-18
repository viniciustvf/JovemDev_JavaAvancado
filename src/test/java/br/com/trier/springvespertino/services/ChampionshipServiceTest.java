package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTests;
import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import jakarta.transaction.Transactional;

@Transactional
public class ChampionshipServiceTest extends BaseTests {

	@Autowired
	ChampionshipService championshipService;

	@Test
	@DisplayName("Teste buscar campeonato por ID")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByIdTest() {
		var campeonato = championshipService.findById(1);
		assertNotNull(campeonato);
		assertEquals(1, campeonato.getId());
		assertEquals("Campeonato 1", campeonato.getDescription());
	}

	@Test
	@DisplayName("Teste buscar campeonato por ID inexistente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByIdWrongTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> championshipService.findById(10));
		assertEquals("O campeonato 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar campeonato por nome")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByDescriptionTest() {
		var campeonato = championshipService.findByDescriptionIgnoreCase("Campeonato 1");
		assertEquals(1, campeonato.size());
		var exception = assertThrows(ObjectNotFound.class,
				() -> championshipService.findByDescriptionIgnoreCase("campi"));
		assertEquals("Nenhum campeonato campi cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar campeonato por letra que começa")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByDescriptionStartingWithWrongTest() {
		var lista = championshipService.findByDescriptionStartingWithIgnoreCase("c");
		assertEquals(3, lista.size());
		var exception = assertThrows(ObjectNotFound.class,
				() -> championshipService.findByDescriptionStartingWithIgnoreCase("z"));
		assertEquals("Nenhum campeonato inicia com z cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir campeonato")
	void insertChampionshipTest() {
		Championship campeonato = new Championship(null, "insert", 2024);
		championshipService.insert(campeonato);
		campeonato = championshipService.findById(campeonato.getId());
		assertEquals(2024, campeonato.getYear());
		assertEquals("insert", campeonato.getDescription());
	}

	@Test
	@DisplayName("Teste apagar campeonato")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void deleteByIdTest() {
		championshipService.delete(1);
		List<Championship> list = championshipService.listAll();
		assertEquals(2, list.size());
	}

	@Test
	@DisplayName("Teste apagar campeonato inexistente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void deleteByIdNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> championshipService.delete(10));
		assertEquals("O campeonato 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste alterar campeonato inexistente")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void updateChampionshipNonExistsTest() {
		Championship campeonato = new Championship(20, "update", 2000);
		var exception = assertThrows(ObjectNotFound.class, () -> championshipService.update(campeonato));
		assertEquals("O campeonato 20 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste listar todos sem campeonato cadastrados")
	void listAllNonExistsChampionshipTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> championshipService.listAll());
		assertEquals("Nenhum campeonato cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir campeonato com ano errado")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void insertChampionshipYearWrongTest() {
		Championship campeonato1 = new Championship(null, "update", 2030);
		var exception1 = assertThrows(IntegrityViolation.class, () -> championshipService.insert(campeonato1));
		assertEquals("Ano deve ser maior que 1990 e menor que 2024", exception1.getMessage());
		Championship campeonato2 = new Championship(null, "update", 1989);
		var exception2 = assertThrows(IntegrityViolation.class, () -> championshipService.insert(campeonato2));
		assertEquals("Ano deve ser maior que 1990 e menor que 2024", exception2.getMessage());
	}

	@Test
	@DisplayName("Teste alterar campeonato")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void updateChampionshipTest() {
		Championship campeonato = new Championship(1, "update", 2003);
		championshipService.update(campeonato);
		assertEquals(1, campeonato.getId());
		assertEquals("update", campeonato.getDescription());
		assertEquals(2003, campeonato.getYear());
	}

	@Test
	@DisplayName("Teste listar todos")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void listAllTest() {
		List<Championship> lista = championshipService.listAll();
		assertEquals(3, lista.size());
	}

	@Test
	@DisplayName("Teste buscar campeonato por ano")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByYearTest() {
		var lista = championshipService.findByYear(2020);
		assertEquals(1, lista.size());
		var exception = assertThrows(IntegrityViolation.class, () -> championshipService.findByYear(2030));
		assertEquals("Ano deve ser maior que 1990 e menor que %s".formatted(LocalDateTime.now().plusYears(1).getYear()),
				exception.getMessage());
		var exception2 = assertThrows(ObjectNotFound.class, () -> championshipService.findByYear(2000));
		assertEquals("Nenhum campeonato em 2000 cadastrado", exception2.getMessage());
	}

	@Test
	@DisplayName("Teste buscar campeonato por ano between")
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	void findByYearBetweenTest() {
		var lista = championshipService.findByYearBetween(2010, 2024);
		assertEquals(3, lista.size());
		var exception = assertThrows(IntegrityViolation.class, () -> championshipService.findByYearBetween(2030, 1989));
		assertEquals("Ano deve ser maior que 1990 e menor que %s".formatted(LocalDateTime.now().plusYears(1).getYear()),
				exception.getMessage());
		var exception2 = assertThrows(IntegrityViolation.class,
				() -> championshipService.findByYearBetween(null, null));
		assertEquals("Ano não pode ser nulo", exception2.getMessage());
		var exception3 = assertThrows(ObjectNotFound.class, () -> championshipService.findByYearBetween(2000, 2001));
		assertEquals("Nenhum campeonato entre 2000 e 2001 cadastrado", exception3.getMessage());

	}

}