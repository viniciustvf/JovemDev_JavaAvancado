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
import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.models.User;
import jakarta.transaction.Transactional;

@Transactional
public class TeamServiceTest extends BaseTests{

	@Autowired
	TeamService teamService;
	
	@Test
    @DisplayName("Teste buscar time por ID")
    @Sql({"classpath:/resources/sqls/time.sql"})
    void findByIdTest() {
        var team = teamService.findById(1);
        assertNotNull(team);
        assertEquals(1, team.getId());
        assertEquals("Time 1", team.getName());
    }
	
	@Test
    @DisplayName("Teste buscar time por ID inexistente")
    @Sql({"classpath:/resources/sqls/time.sql"})
    void findByIdWrongTest() {
        var team = teamService.findById(10);
        assertNull(team);
    }
	
	@Test
    @DisplayName("Teste buscar time por nome")
    @Sql({"classpath:/resources/sqls/time.sql"})
    void findByNameTest() {
        var team = teamService.findByName("Time 1");
        assertNotNull(team);
        assertEquals(1, team.size());
    }
	
	@Test
    @DisplayName("Teste buscar time por nome inexistente")
    @Sql({"classpath:/resources/sqls/time.sql"})
    void findByNameWrongTest() {
        var time = teamService.findByName("c");
        assertEquals(0, time.size());
    }
	
	@Test
    @DisplayName("Teste buscar time por letra que começa")
    @Sql({"classpath:/resources/sqls/time.sql"})
    void findByNameStartingWithTest() {
        var list = teamService.findByNameStartingWithIgnoreCase("t");
        assertNotNull(list);
        assertEquals(2, list.size());
    }
	
	@Test
    @DisplayName("Teste buscar time por letra que começa inexistente")
    @Sql({"classpath:/resources/sqls/time.sql"})
    void findByNameStartingWithWrongTest() {
        var list = teamService.findByNameStartingWithIgnoreCase("z");
        assertEquals(0, list.size());
    }
	
	@Test
    @DisplayName("Teste inserir time")
    void insertTeamTest() {
        Team team = new Team(null, "insert");
        teamService.insert(team);
        team = teamService.findById(1);
        assertEquals(1, team.getId());
        assertEquals("insert", team.getName());
    }
	
	@Test
    @DisplayName("Teste apagar time por ID")
    @Sql({"classpath:/resources/sqls/time.sql"})
    void deleteByIdTest() {
        teamService.delete(1);
        List<Team> list = teamService.listAll();
        assertEquals(1, list.size());
    }
	
	@Test
    @DisplayName("Teste apagar time por ID incorreto")
    @Sql({"classpath:/resources/sqls/time.sql"})
    void deleteByIdNonExistsTest() {
        teamService.delete(10);
        List<Team> list = teamService.listAll();
        assertEquals(2, list.size());
    }
	
	@Test
    @DisplayName("Teste alterar time")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void updateByIdTest() {
    	Team team = new Team(1, "update");
    	teamService.update(team);
    	assertEquals("update", team.getName());
    	assertEquals(1, team.getId());
    }
	
	
	
	
	
}
