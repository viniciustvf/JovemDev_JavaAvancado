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
import br.com.trier.springvespertino.models.PilotRace;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import jakarta.transaction.Transactional;

@Transactional
public class PilotRaceServiceTest extends BaseTests {

	@Autowired
	PilotRaceService pilotRaceService;

	@Autowired
	PilotService pilotService;

	@Autowired
	RaceService raceService;

	@Test
	@DisplayName("Teste buscar piloto por ID")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	@Sql({ "classpath:/resources/sqls/time.sql" })
	@Sql({ "classpath:/resources/sqls/piloto.sql" })
	@Sql({ "classpath:/resources/sqls/piloto_corrida.sql" })
	void findByIdTest() {
		var pilotRace = pilotRaceService.findById(1);
		assertNotNull(pilotRace);
		assertEquals(1, pilotRace.getId());
		assertEquals(1, pilotRace.getPlacing());
		assertEquals(1, pilotRace.getRace().getId());
	}

	@Test
	@DisplayName("Teste buscar piloto por ID inexistente")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	@Sql({ "classpath:/resources/sqls/time.sql" })
	@Sql({ "classpath:/resources/sqls/piloto.sql" })
	@Sql({ "classpath:/resources/sqls/piloto_corrida.sql" })
	void findByIdWrongTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> pilotRaceService.findById(10));
		assertEquals("A corrida piloto 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir piloto corrida")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	@Sql({ "classpath:/resources/sqls/time.sql" })
	@Sql({ "classpath:/resources/sqls/piloto.sql" })
	void insertPilotRaceTest() {
		PilotRace pilotRace = new PilotRace(null, pilotService.findById(1), raceService.findById(1), 1);
		pilotRaceService.insert(pilotRace);
		pilotRace = pilotRaceService.findById(pilotRace.getId());
		assertEquals(1, pilotRace.getId());
		assertEquals(1, pilotRace.getPilot().getId());
		var exception = assertThrows(IntegrityViolation.class,
				() -> pilotRaceService.insert(new PilotRace(null, null, raceService.findById(1), 1)));
		assertEquals("O piloto não pode ser nulo", exception.getMessage());
		var exception2 = assertThrows(IntegrityViolation.class,
				() -> pilotRaceService.insert(new PilotRace(null, pilotService.findById(1), null, 1)));
		assertEquals("A corrida não pode ser nula", exception2.getMessage());
		var exception3 = assertThrows(IntegrityViolation.class, () -> pilotRaceService
				.insert(new PilotRace(null, pilotService.findById(1), raceService.findById(1), null)));
		assertEquals("A colocação não pode ser nula", exception3.getMessage());
	}

	@Test
	@DisplayName("Teste listar todos")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	@Sql({ "classpath:/resources/sqls/time.sql" })
	@Sql({ "classpath:/resources/sqls/piloto.sql" })
	@Sql({ "classpath:/resources/sqls/piloto_corrida.sql" })
	void listAllTest() {
		List<PilotRace> lista = pilotRaceService.listAll();
		assertEquals(2, lista.size());
	}

	@Test
	@DisplayName("Teste listar todos sem piloto corrida cadastrado")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	@Sql({ "classpath:/resources/sqls/time.sql" })
	@Sql({ "classpath:/resources/sqls/piloto.sql" })
	void listAllWrongTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> pilotRaceService.listAll());
		assertEquals("Nenhum piloto corrida cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste alterar piloto corrida")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	@Sql({ "classpath:/resources/sqls/time.sql" })
	@Sql({ "classpath:/resources/sqls/piloto.sql" })
	@Sql({ "classpath:/resources/sqls/piloto_corrida.sql" })
	void updatePilotRaceTest() {
		PilotRace pilotRace = new PilotRace(1, pilotService.findById(2), raceService.findById(2), 2);
		pilotRaceService.update(pilotRace);
		pilotRace = pilotRaceService.findById(pilotRace.getId());
		assertEquals(1, pilotRace.getId());
		assertEquals(2, pilotRace.getPilot().getId());
	}

	@Test
	@DisplayName("Teste apagar piloto corrida")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	@Sql({ "classpath:/resources/sqls/time.sql" })
	@Sql({ "classpath:/resources/sqls/piloto.sql" })
	@Sql({ "classpath:/resources/sqls/piloto_corrida.sql" })
	void deleteByIdTest() {
		pilotRaceService.delete(1);
		List<PilotRace> list = pilotRaceService.listAll();
		assertEquals(1, list.size());
	}

	@Test
	@DisplayName("Teste buscar piloto corrida por colocação")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	@Sql({ "classpath:/resources/sqls/time.sql" })
	@Sql({ "classpath:/resources/sqls/piloto.sql" })
	@Sql({ "classpath:/resources/sqls/piloto_corrida.sql" })
	void findByPlacingTest() {
		var lista = pilotRaceService.findByPlacing(1);
		assertEquals(1, lista.size());
		var exception = assertThrows(ObjectNotFound.class, () -> pilotRaceService.findByPlacing(10));
		assertEquals("Nenhuma corrida piloto encontrada para a colocação 10", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar piloto corrida por colocação between")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	@Sql({ "classpath:/resources/sqls/time.sql" })
	@Sql({ "classpath:/resources/sqls/piloto.sql" })
	@Sql({ "classpath:/resources/sqls/piloto_corrida.sql" })
	void findByPlacingBetweenTest() {
		var lista = pilotRaceService.findByPlacingBetween(0, 3);
		assertEquals(2, lista.size());
		var exception = assertThrows(ObjectNotFound.class, () -> pilotRaceService.findByPlacingBetween(10, 3));
		assertEquals("Nenhuma corrida piloto encontrada entre a colocação 10° e 3°", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar piloto corrida por piloto")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	@Sql({ "classpath:/resources/sqls/time.sql" })
	@Sql({ "classpath:/resources/sqls/piloto.sql" })
	@Sql({ "classpath:/resources/sqls/piloto_corrida.sql" })
	void findByPilotTest() {
		var lista = pilotRaceService.findByPilot(pilotService.findById(1));
		assertEquals(1, lista.size());
		pilotRaceService.delete(1);
		var exception = assertThrows(ObjectNotFound.class,
				() -> pilotRaceService.findByPilot(pilotService.findById(1)));
		assertEquals("Nenhuma corrida piloto encontrada para o piloto Piloto 1", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar piloto corrida por corrida")
	@Sql({ "classpath:/resources/sqls/pais.sql" })
	@Sql({ "classpath:/resources/sqls/pista.sql" })
	@Sql({ "classpath:/resources/sqls/campeonato.sql" })
	@Sql({ "classpath:/resources/sqls/corrida.sql" })
	@Sql({ "classpath:/resources/sqls/time.sql" })
	@Sql({ "classpath:/resources/sqls/piloto.sql" })
	@Sql({ "classpath:/resources/sqls/piloto_corrida.sql" })
	void findByRaceTest() {
		var lista = pilotRaceService.findByRace(raceService.findById(1));
		assertEquals(1, lista.size());
		pilotRaceService.delete(1);
		var exception = assertThrows(ObjectNotFound.class, () -> pilotRaceService.findByRace(raceService.findById(1)));
		assertEquals("Nenhuma corrida piloto encontrada para a corrida 1", exception.getMessage());
	}

}