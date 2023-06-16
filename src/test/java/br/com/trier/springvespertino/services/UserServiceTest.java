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
import br.com.trier.springvespertino.models.User;
import jakarta.transaction.Transactional;

@Transactional
public class UserServiceTest extends BaseTests {
	
	@Autowired
	UserService userService;
	
	@Test
    @DisplayName("Teste buscar usuário por ID")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByIdTest() {
        var usuario = userService.findById(1);
        assertNotNull(usuario);
        assertEquals(1, usuario.getId());
        assertEquals("User 1", usuario.getName());
        assertEquals("Email 1", usuario.getEmail());
        assertEquals("Senha 1", usuario.getPassword());
    }
    
    @Test
    @DisplayName("Teste buscar usuário por ID inexistente")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByIdWrongTest() {
        var usuario = userService.findById(4);
        assertNull(usuario);
    }
    
    @Test
    @DisplayName("Teste buscar usuario por nome")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByNameTest() {
        var usuario = userService.findByName("User 1");
        assertNotNull(usuario);
        assertEquals(1, usuario.size());
    }
    
    @Test
    @DisplayName("Teste buscar usuario por nome inexistente")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByNameWrongTest() {
        var usuario = userService.findByName("c");
        assertEquals(0, usuario.size());
    }
    
    @Test
    @DisplayName("Teste buscar usuario por letra que começa")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void findByNameStartingWithTest() {
        List<User> list = userService.findByNameStartingWithIgnoreCase("u");
        assertNotNull(list);
        assertEquals(2, list.size());
    }
    
    @Test
    @DisplayName("Teste inserir usuario")
    void insertUserTest() {
        User usuario = new User(null, "insert", "insert", "insert");
        userService.insert(usuario);
        usuario = userService.findById(1);
        assertEquals(1, usuario.getId());
        assertEquals("insert", usuario.getName());
        assertEquals("insert", usuario.getEmail());
        assertEquals("insert", usuario.getPassword());
    }
    
    @Test
    @DisplayName("Teste apagar usuario por ID")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void deleteByIdTest() {
        userService.delete(1);
        List<User> list = userService.listAll();
        assertEquals(1, list.size());
    }
    
    @Test
    @DisplayName("Teste apagar usuario por ID incorreto")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void deleteByIdNonExistsTest() {
        userService.delete(10);
        List<User> list = userService.listAll();
        assertEquals(2, list.size());
    }
    
    @Test
    @DisplayName("Teste alterar usuario")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void updateByIdTest() {
    	User usuario = new User(1, "update", "update", "update");
    	userService.update(usuario);
    	assertEquals("update", usuario.getName());
    	assertEquals("update", usuario.getEmail());
    	assertEquals("update", usuario.getPassword());
    	assertEquals(1, usuario.getId());
    }
}
