package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTests;
import br.com.trier.springvespertino.models.Championship;
import jakarta.transaction.Transactional;

@Transactional
public class ChampionshipServiceTest extends BaseTests {

	@Autowired
	ChampionshipService championshipService;
	
	@Test
	@DisplayName("Teste buscar campeonato por ID")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByIdTest() {
        var championship = championshipService.findById(1);
        assertNotNull(championship);
        assertEquals(1, championship.getId());
        assertEquals("Campeonato 1", championship.getDescription());
    }
	
	@Test
	@DisplayName("Teste buscar campeonato por ID inexistente")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByIdWrongTest() {
        var championship = championshipService.findById(10);
        assertNull(championship);
    }
	
	@Test
	@DisplayName("Teste buscar campeonato por nome")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByDescriptionTest() {
        var championship = championshipService.findByDescription("Campeonato 1");
        assertNotNull(championship);
        assertEquals(1, championship.size());
    }
	
	@Test
	@DisplayName("Teste buscar campeonato por nome inexistente")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByDescriptionWrongTest() {
        var campeonato = championshipService.findByDescription("z");
        assertEquals(0, campeonato.size());
    }
	
	@Test
	@DisplayName("Teste buscar campeonato por letra que começa")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByDescriptionStartingWithTest() {
        var list = championshipService.findByDescriptionStartingWithIgnoreCase("c");
        assertNotNull(list);
        assertEquals(2, list.size());
    }
	
	@Test
    @DisplayName("Teste buscar campeonato por letra que começa inexistente")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByDescriptionStartingWithWrongTest() {
        var list = championshipService.findByDescriptionStartingWithIgnoreCase("z");
        assertEquals(0, list.size());
    }
	
	@Test
    @DisplayName("Teste inserir campeonato")
    void insertChampionshipTest() {
        Championship championship = new Championship(null, "insert", 2000);
        championshipService.insert(championship);
        championship = championshipService.findById(2);
        assertEquals(2, championship.getId());
        assertEquals(2000, championship.getYear());
        assertEquals("insert", championship.getDescription());
    }
	
	@Test
    @DisplayName("Teste apagar campeonato por ID")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void deleteByIdTest() {
        championshipService.delete(1);
        List<Championship> list = championshipService.listAll();
        assertEquals(1, list.size());
    }
	
	@Test
    @DisplayName("Teste apagar campeonato por ID incorreto")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void deleteByIdNonExistsTest() {
        championshipService.delete(10);
        List<Championship> list = championshipService.listAll();
        assertEquals(2, list.size());
    }
	
	@Test
    @DisplayName("Teste alterar campeonato")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void updateByIdTest() {
    	Championship championship = new Championship(1, "update", 2010);
    	championshipService.update(championship);
    	assertEquals("update", championship.getDescription());
    	assertEquals(2010, championship.getYear());
    	assertEquals(1, championship.getId());
    }
	
	@Test
	@DisplayName("Teste buscar campeonato por ano")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByYearTest() {
        var championship = championshipService.findByYear(2020);
        assertNotNull(championship);
        assertEquals(1, championship.size());
    }
	
	@Test
	@DisplayName("Teste buscar campeonato por ano inexistente")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByYearWrongTest() {
        var championship = championshipService.findByYear(1111);
        assertEquals(0, championship.size());
    }
	
	@Test
	@DisplayName("Teste buscar campeonato entre intervalo de anos")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByYearBetweenTest() {
        List<Championship> championship = championshipService.findByYearBetween(2000, 2030);
        assertNotNull(championship);
        assertEquals(2, championship.size());
        championship = championshipService.findByYearBetween(2021, 2030);
        assertNotNull(championship);
        assertEquals(1, championship.size());
    }
	
	@Test
	@DisplayName("Teste buscar campeonato entre intervalo de anos inexistente")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByYearBetweenWrongTest() {
        List<Championship> championship = championshipService.findByYearBetween(9999, 9999);
        assertEquals(0, championship.size());
        championship = championshipService.findByYearBetween(2320, 2030);
        assertEquals(0, championship.size());
        championship = championshipService.findByYearBetween(2021, 2022);
        assertEquals(0, championship.size());
    }	
}
