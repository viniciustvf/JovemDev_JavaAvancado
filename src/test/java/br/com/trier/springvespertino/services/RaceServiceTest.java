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
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import jakarta.transaction.Transactional;

@Transactional
public class RaceServiceTest extends BaseTests {
	
	@Autowired
	RaceService raceService;
	
	@Test
    @DisplayName("Teste buscar raceo por ID")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void findByIdTest() {
        var raceo = raceService.findById(1);
        assertNotNull(raceo);
        assertEquals(1, raceo.getId());
        assertEquals("Time 1", raceo.getName());
    }
    
    @Test
    @DisplayName("Teste buscar raceo por ID inexistente")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void findByIdWrongTest() {
        var exception = assertThrows(ObjectNotFound.class,() -> raceService.findById(10));
        assertEquals("O raceo 10 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar raceo por nome")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void findByNameTest() {
        var raceo = raceService.findByNameIgnoreCase("Time 1");
        assertEquals("Time 1", raceo.getName());
        var exception = assertThrows(ObjectNotFound.class, () -> raceService.findByNameIgnoreCase("timi"));
        assertEquals("Nenhum raceo timi cadastrado", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar raceo por letra que começa errada")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void findByNameStartingWithWrongTest() {
        var lista = raceService.findByNameStartingWithIgnoreCase("t");
        assertEquals(2, lista.size());
        var exception = assertThrows(ObjectNotFound.class, () -> raceService.findByNameStartingWithIgnoreCase("z"));
        assertEquals("Nenhum nome de raceo inicia com z cadastrado", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste inserir raceo")
    void insertRaceTest() {
        Race raceo = new Race(null, "insert");
        raceService.insert(raceo);
        raceo = raceService.findById(raceo.getId());
        assertEquals(1, raceo.getId());
        assertEquals("insert", raceo.getName());
    }
    
    @Test
    @DisplayName("Teste apagar raceo")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void deleteByIdTest() {
        raceService.delete(1);
        List<Race> list = raceService.listAll();
        assertEquals(1, list.size());
    }
    
    @Test
    @DisplayName("Teste apagar raceo inexistente")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void deleteByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class,() -> raceService.delete(10));
        assertEquals("O raceo 10 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste alterar raceo inexistente")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void updateRaceNonExistsTest() {
    	Race raceo = new Race(20, "update");
    	var exception = assertThrows(ObjectNotFound.class,() -> raceService.update(raceo));
        assertEquals("O raceo 20 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste listar todos sem raceos cadastrados")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void listAllNonExistsRaceTest() {
    	raceService.delete(1);
    	raceService.delete(2);
    	var exception = assertThrows(ObjectNotFound.class,() -> raceService.listAll());
        assertEquals("Nenhum raceo cadastrado", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste inserir raceo com nome duplicado")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void insertRaceTimeDuplicateTest() {
        Race raceo = new Race(null, "Time 1");
        var exception = assertThrows(IntegrityViolation.class,() -> raceService.insert(raceo));
        assertEquals("Nome já existente: Time 1", exception.getMessage());
 
    }
    
    @Test
    @DisplayName("Teste alterar raceo com nome duplicado")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void updateRaceTimeWrongTest() {
    	Race raceo = new Race(2, "Time 1");
        var exception = assertThrows(IntegrityViolation.class, () -> raceService.update(raceo));
        assertEquals("Nome já existente: Time 1", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste alterar raceo")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void updateRaceTest() {
        Race raceo = new Race(1, "update");
        raceService.update(raceo);
        assertEquals(1, raceo.getId());
        assertEquals("update", raceo.getName());
    }
    
    @Test
    @DisplayName("Teste listar todos")
    @Sql({"classpath:/resources/sqls/raceo.sql"})
    void listAllTest() {
    	List<Race> lista = raceService.listAll();
    	assertEquals(2, lista.size());
    }
}