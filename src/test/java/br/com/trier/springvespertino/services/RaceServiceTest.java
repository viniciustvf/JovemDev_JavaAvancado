package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTests;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import jakarta.transaction.Transactional;

@Transactional
public class RaceServiceTest extends BaseTests {
	
	@Autowired
	RaceService raceService;
	
	@Autowired
	TrackService trackService;
	
	@Autowired
	ChampionshipService championshipService;
	
	@Test
    @DisplayName("Teste buscar corrida por ID")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"})
    void findByIdTest() {
        var race = raceService.findById(1);
        assertNotNull(race);
        assertEquals(1, race.getId());
        assertEquals(ZonedDateTime.of(2020, 6, 22, 15, 25, 0, 100000000, ZoneOffset.ofHours(-3)), race.getDate());
    }
    
	@Test
    @DisplayName("Teste buscar corrida por ID inexistente")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"})
    void findByIdWrongTest() {
        var exception = assertThrows(ObjectNotFound.class,() -> raceService.findById(10));
        assertEquals("A corrida 10 não existe", exception.getMessage());
    }
    
	@Test
    @DisplayName("Teste buscar corrida por data")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"})
    void findByDateTest() {
        var race = raceService.findByDate(ZonedDateTime.of(2020, 6, 22, 15, 25, 0, 100000000, ZoneOffset.ofHours(-3)));
        assertNotNull(race);
        assertEquals(1, race.size());
    }
	
	@Test
    @DisplayName("Teste buscar corrida por data errada")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"})
    void findByDateWrongTest() {
        var exception = assertThrows(ObjectNotFound.class, () -> raceService.findByDate(ZonedDateTime.of(2024, 6, 22, 15, 25, 0, 100000000, ZoneOffset.ofHours(-3))));
        assertEquals("Nenhuma corrida encontrada para a data 2024-06-22T15:25:00.100-03:00", exception.getMessage());
    }
	
	@Test
    @DisplayName("Teste buscar corridas por pista")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"})
    void findByTrackTest() {
		var lista = raceService.findByTrack(trackService.findById(1));
        assertEquals(1, lista.size());
        var exception = assertThrows(ObjectNotFound.class, () -> raceService.findByTrack(trackService.findById(6)));
        assertEquals("A pista 6 não existe", exception.getMessage());
        var exception2 = assertThrows(ObjectNotFound.class, () -> raceService.findByTrack(trackService.findById(3)));
        assertEquals("Nenhuma corrida encontrada para a pista 3", exception2.getMessage());
    }
	
	@Test
    @DisplayName("Teste buscar corridas por campeonato")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"})
    void findByChampionshipTest() {
		var lista = raceService.findByChampionship(championshipService.findById(1));
        assertEquals(1, lista.size());
        var exception = assertThrows(ObjectNotFound.class, () -> raceService.findByChampionship(championshipService.findById(6)));
        assertEquals("O campeonato 6 não existe", exception.getMessage());
        var exception2 = assertThrows(ObjectNotFound.class, () -> raceService.findByChampionship(championshipService.findById(3)));
        assertEquals("Nenhuma corrida encontrada para o campeonato 3", exception2.getMessage());
    }
    
	@Test
    @DisplayName("Teste inserir corrida")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
    void insertRaceTest() {
        Race corrida = new Race(1, ZonedDateTime.of(2012, 6, 22, 15, 25, 0, 100000000, ZoneOffset.ofHours(-3)), trackService.findById(3), championshipService.findById(3));
        raceService.insert(corrida);
        corrida = raceService.findById(corrida.getId());
        assertEquals(1, corrida.getId());
        assertEquals(2012, corrida.getDate().getYear());
    }
	
	@Test
    @DisplayName("Teste inserir corrida com ano diferente do campeonato")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
    void insertRaceWrongDateTest() {
		Race corrida = new Race(1, ZonedDateTime.of(2015, 6, 22, 15, 25, 0, 100000000, ZoneOffset.ofHours(-3)), trackService.findById(3), championshipService.findById(3));
		var exception = assertThrows(IntegrityViolation.class, () -> raceService.insert(corrida));
        assertEquals("A data da corrida deve corresponder ao ano do campeonato", exception.getMessage());
    }
	
	@Test
    @DisplayName("Teste inserir corrida com cameponato nulo")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
    void insertRaceWrongChampTest() {
		Race corrida = new Race(1, ZonedDateTime.of(2015, 6, 22, 15, 25, 0, 100000000, ZoneOffset.ofHours(-3)), trackService.findById(3), null);
		var exception = assertThrows(IntegrityViolation.class, () -> raceService.insert(corrida));
        assertEquals("O campeonato não pode ser nulo", exception.getMessage());
    }
	
	@Test
    @DisplayName("Teste inserir corrida com pista nula")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
    void insertRaceWrongTrackTest() {
		Race corrida = new Race(1, ZonedDateTime.of(2012, 6, 22, 15, 25, 0, 100000000, ZoneOffset.ofHours(-3)), null, championshipService.findById(3));
		var exception = assertThrows(IntegrityViolation.class, () -> raceService.insert(corrida));
        assertEquals("A pista não pode ser nula", exception.getMessage());
    }
    
	@Test
    @DisplayName("Teste listar todos")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"})
    void listAllTest() {
    	List<Race> lista = raceService.listAll();
    	assertEquals(2, lista.size());
    }
	
	@Test
    @DisplayName("Teste listar todos sem corrida cadastrada")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
    void listAllWrongTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> raceService.listAll());
        assertEquals("Nenhuma corrida cadastrada", exception.getMessage());
    }
	
	@Test
    @DisplayName("Teste alterar corrida")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"})
    void updateTeamTest() {
		Race corrida = new Race(1, ZonedDateTime.of(2020, 6, 22, 15, 25, 0, 100000000, ZoneOffset.ofHours(-3)), trackService.findById(2), championshipService.findById(1));
        raceService.update(corrida);
        assertEquals(1, corrida.getId());
        assertEquals("Pista 2", corrida.getTrack().getName());
    }
	
	@Test
    @DisplayName("Teste apagar corrida")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	@Sql({"classpath:/resources/sqls/campeonato.sql"})
	@Sql({"classpath:/resources/sqls/corrida.sql"}) 
    void deleteByIdTest() {
        raceService.delete(1);
        List<Race> list = raceService.listAll();
        assertEquals(1, list.size());
    }
	
	
	
	
	
}