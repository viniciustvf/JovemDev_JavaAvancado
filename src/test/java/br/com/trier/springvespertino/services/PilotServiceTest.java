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
	
	@Test
    @DisplayName("Teste buscar piloto por ID")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByIdTest() {
        var piloto = pilotService.findById(1);
        assertNotNull(piloto);
        assertEquals(1, piloto.getId());
        assertEquals("Time 1", piloto.getName());
    }
    
    @Test
    @DisplayName("Teste buscar piloto por ID inexistente")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByIdWrongTest() {
        var exception = assertThrows(ObjectNotFound.class,() -> pilotService.findById(10));
        assertEquals("O piloto 10 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar piloto por nome")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByNameTest() {
        var piloto = pilotService.findByNameIgnoreCase("Time 1");
        assertEquals("Time 1", piloto.getName());
        var exception = assertThrows(ObjectNotFound.class, () -> pilotService.findByNameIgnoreCase("timi"));
        assertEquals("Nenhum piloto timi cadastrado", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar piloto por letra que começa errada")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByNameStartingWithWrongTest() {
        var lista = pilotService.findByNameStartingWithIgnoreCase("t");
        assertEquals(2, lista.size());
        var exception = assertThrows(ObjectNotFound.class, () -> pilotService.findByNameStartingWithIgnoreCase("z"));
        assertEquals("Nenhum nome de piloto inicia com z cadastrado", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste inserir piloto")
    void insertPilotTest() {
        Pilot piloto = new Pilot(null, "insert");
        pilotService.insert(piloto);
        piloto = pilotService.findById(piloto.getId());
        assertEquals(1, piloto.getId());
        assertEquals("insert", piloto.getName());
    }
    
    @Test
    @DisplayName("Teste apagar piloto")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void deleteByIdTest() {
        pilotService.delete(1);
        List<Pilot> list = pilotService.listAll();
        assertEquals(1, list.size());
    }
    
    @Test
    @DisplayName("Teste apagar piloto inexistente")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void deleteByIdNonExistsTest() {
        var exception = assertThrows(ObjectNotFound.class,() -> pilotService.delete(10));
        assertEquals("O piloto 10 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste alterar piloto inexistente")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void updatePilotNonExistsTest() {
    	Pilot piloto = new Pilot(20, "update");
    	var exception = assertThrows(ObjectNotFound.class,() -> pilotService.update(piloto));
        assertEquals("O piloto 20 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste listar todos sem pilotos cadastrados")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void listAllNonExistsPilotTest() {
    	pilotService.delete(1);
    	pilotService.delete(2);
    	var exception = assertThrows(ObjectNotFound.class,() -> pilotService.listAll());
        assertEquals("Nenhum piloto cadastrado", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste inserir piloto com nome duplicado")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void insertPilotTimeDuplicateTest() {
        Pilot piloto = new Pilot(null, "Time 1");
        var exception = assertThrows(IntegrityViolation.class,() -> pilotService.insert(piloto));
        assertEquals("Nome já existente: Time 1", exception.getMessage());
 
    }
    
    @Test
    @DisplayName("Teste alterar piloto com nome duplicado")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void updatePilotTimeWrongTest() {
    	Pilot piloto = new Pilot(2, "Time 1");
        var exception = assertThrows(IntegrityViolation.class, () -> pilotService.update(piloto));
        assertEquals("Nome já existente: Time 1", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste alterar piloto")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void updatePilotTest() {
        Pilot piloto = new Pilot(1, "update");
        pilotService.update(piloto);
        assertEquals(1, piloto.getId());
        assertEquals("update", piloto.getName());
    }
    
    @Test
    @DisplayName("Teste listar todos")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void listAllTest() {
    	List<Pilot> lista = pilotService.listAll();
    	assertEquals(2, lista.size());
    }
}