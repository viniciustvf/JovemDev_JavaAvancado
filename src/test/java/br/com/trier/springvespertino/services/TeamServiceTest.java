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
import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import jakarta.transaction.Transactional;

@Transactional
public class TeamServiceTest extends BaseTests {

	@Autowired
	TeamService teamService;

	@Test
	@DisplayName("Teste buscar time por ID")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void findByIdTest() {
		var time = teamService.findById(1);
		assertNotNull(time);
		assertEquals(1, time.getId());
		assertEquals("Time 1", time.getName());
	}

	@Test
	@DisplayName("Teste buscar time por ID inexistente")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void findByIdWrongTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> teamService.findById(10));
		assertEquals("O time 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar time por nome")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void findByNameTest() {
		var time = teamService.findByNameIgnoreCase("Time 1");
		assertEquals("Time 1", time.getName());
		var exception = assertThrows(ObjectNotFound.class, () -> teamService.findByNameIgnoreCase("timi"));
		assertEquals("Nenhum time timi cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar time por letra que começa errada")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void findByNameStartingWithWrongTest() {
		var lista = teamService.findByNameStartingWithIgnoreCase("t");
		assertEquals(3, lista.size());
		var exception = assertThrows(ObjectNotFound.class, () -> teamService.findByNameStartingWithIgnoreCase("z"));
		assertEquals("Nenhum nome de time inicia com z cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir time")
	void insertTeamTest() {
		Team time = new Team(null, "insert");
		teamService.insert(time);
		time = teamService.findById(time.getId());
		assertEquals(1, time.getId());
		assertEquals("insert", time.getName());
	}

	@Test
	@DisplayName("Teste apagar time")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void deleteByIdTest() {
		teamService.delete(1);
		List<Team> list = teamService.listAll();
		assertEquals(2, list.size());
	}

	@Test
	@DisplayName("Teste apagar time inexistente")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void deleteByIdNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> teamService.delete(10));
		assertEquals("O time 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste alterar time inexistente")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void updateTeamNonExistsTest() {
		Team time = new Team(20, "update");
		var exception = assertThrows(ObjectNotFound.class, () -> teamService.update(time));
		assertEquals("O time 20 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste listar todos sem times cadastrados")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void listAllNonExistsTeamTest() {
		teamService.delete(1);
		teamService.delete(2);
		teamService.delete(3);
		var exception = assertThrows(ObjectNotFound.class, () -> teamService.listAll());
		assertEquals("Nenhum time cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir time com nome duplicado")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void insertTeamTimeDuplicateTest() {
		Team time = new Team(null, "Time 1");
		var exception = assertThrows(IntegrityViolation.class, () -> teamService.insert(time));
		assertEquals("Nome já existente: Time 1", exception.getMessage());

	}

	@Test
	@DisplayName("Teste alterar time com nome duplicado")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void updateTeamTimeWrongTest() {
		Team time = new Team(2, "Time 1");
		var exception = assertThrows(IntegrityViolation.class, () -> teamService.update(time));
		assertEquals("Nome já existente: Time 1", exception.getMessage());
	}

	@Test
	@DisplayName("Teste alterar time")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void updateTeamTest() {
		Team time = new Team(1, "update");
		teamService.update(time);
		assertEquals(1, time.getId());
		assertEquals("update", time.getName());
	}

	@Test
	@DisplayName("Teste listar todos")
	@Sql({ "classpath:/resources/sqls/time.sql" })
	void listAllTest() {
		List<Team> lista = teamService.listAll();
		assertEquals(3, lista.size());
	}
}