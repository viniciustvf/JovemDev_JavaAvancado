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
import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import jakarta.transaction.Transactional;

@Transactional
public class PilotServiceTest extends BaseTests {
	
	@Autowired
	PilotService pilotService;
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	TeamService teamService;
	
	@Test
    @DisplayName("Teste buscar piloto por ID")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByIdTest() {
        var pilot = pilotService.findById(1);
        assertNotNull(pilot);
        assertEquals(1, pilot.getId());
        assertEquals("Pais 1", pilot.getCountry().getName());
    }
    
	@Test
    @DisplayName("Teste buscar piloto por ID inexistente")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByIdWrongTest() {
        var exception = assertThrows(ObjectNotFound.class,() -> pilotService.findById(10));
        assertEquals("O piloto 10 não existe", exception.getMessage());
    }
    
	@Test
    @DisplayName("Teste inserir piloto")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
    void insertPilotTest() {
        Pilot piloto = new Pilot(null, "Piloto 1", countryService.findById(1),teamService.findById(1));
        pilotService.insert(piloto);
        piloto = pilotService.findById(piloto.getId());
        assertEquals(1, piloto.getId());
        assertEquals("Pais 1", piloto.getCountry().getName());
        var exception = assertThrows(IntegrityViolation.class,() -> pilotService.insert(new Pilot(null, "Piloto 1", null,teamService.findById(1))));
        assertEquals("O país não pode ser nulo", exception.getMessage());
        var exception2 = assertThrows(IntegrityViolation.class,() -> pilotService.insert(new Pilot(null, "Piloto 1", countryService.findById(1),null)));
        assertEquals("O time não pode ser nulo", exception2.getMessage());
    }
	
	@Test
    @DisplayName("Teste alterar piloto")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
    void updatePilotTest() {
        Pilot piloto = new Pilot(1, "Piloto 2", countryService.findById(2),teamService.findById(1));
        pilotService.update(piloto);
        piloto = pilotService.findById(piloto.getId());
        assertEquals(1, piloto.getId());
        assertEquals("Pais 2", piloto.getCountry().getName());
    }
	
	@Test
    @DisplayName("Teste listar todos")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
    void listAllTest() {
    	List<Pilot> lista = pilotService.listAll();
    	assertEquals(2, lista.size());
    }
	
	@Test
    @DisplayName("Teste listar todos sem piloto")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
    void listAllWrongTest() {
    	var exception = assertThrows(ObjectNotFound.class,() -> pilotService.listAll());
        assertEquals("Nenhum piloto cadastrado", exception.getMessage());
    }
	
	@Test
    @DisplayName("Teste apagar corrida")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
    void deleteByIdTest() {
        pilotService.delete(1);
        List<Pilot> list = pilotService.listAll();
        assertEquals(1, list.size());
    }
	
	@Test
    @DisplayName("Teste buscar piloto por nome")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByNameTest() {
        var pilot = pilotService.findByName("Piloto 1");
        assertNotNull(pilot);
        assertEquals(1, pilot.size());
        var exception = assertThrows(ObjectNotFound.class,() -> pilotService.findByName("pilotu 1"));
        assertEquals("Nenhum nome pilotu 1 encontrado", exception.getMessage());
    }
	
	@Test
    @DisplayName("Teste buscar piloto por nome ignore case")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByNameStartingWithIgnoreCaseTest() {
        var pilot = pilotService.findByNameStartingWithIgnoreCase("Pilot");
        assertNotNull(pilot);
        assertEquals(2, pilot.size());
        var exception = assertThrows(ObjectNotFound.class,() -> pilotService.findByNameStartingWithIgnoreCase("pilotu 1"));
        assertEquals("Nenhum nome pilotu 1 encontrado", exception.getMessage());
    }
	
	@Test
    @DisplayName("Teste buscar piloto por país")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByCountryTest() {
        var pilot = pilotService.findByCountry(countryService.findById(1));
        assertNotNull(pilot);
        assertEquals(1, pilot.size());
        var exception = assertThrows(ObjectNotFound.class,() -> pilotService.findByCountry(countryService.findById(3)));
        assertEquals("Nenhum piloto encontrado para o país Pais 3", exception.getMessage());
    }
	
	@Test
    @DisplayName("Teste buscar piloto por time")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/time.sql"})
	@Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByTeamTest() {
        var pilot = pilotService.findByTeam(teamService.findById(1));
        assertNotNull(pilot);
        assertEquals(1, pilot.size());
        var exception = assertThrows(ObjectNotFound.class,() -> pilotService.findByTeam(teamService.findById(3)));
        assertEquals("Nenhum piloto encontrado para o time Time 3", exception.getMessage());
    }
	
	
	
}