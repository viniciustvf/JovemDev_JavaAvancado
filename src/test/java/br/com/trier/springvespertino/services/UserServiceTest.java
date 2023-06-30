package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTests;
import br.com.trier.springvespertino.models.User;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import jakarta.transaction.Transactional;

@Transactional
public class UserServiceTest extends BaseTests {

	@Autowired
	UserService userService;

	@Test
	@DisplayName("Teste buscar usuário por ID")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void findByIdTest() {
		var usuario = userService.findById(3);
		assertNotNull(usuario);
		assertEquals(3, usuario.getId());
		assertEquals("User 1", usuario.getName());
		assertEquals("Email 1", usuario.getEmail());
		assertEquals("Senha 1", usuario.getPassword());
	}

	@Test
	@DisplayName("Teste buscar usuário por ID inexistente")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void findByIdWrongTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> userService.findById(10));
		assertEquals("O usuário 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar usuario por letra que começa errada")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void findByNameStartingWithWrongTest() {
		var lista = userService.findByNameStartingWithIgnoreCase("u");
		assertEquals(2, lista.size());
		var exception = assertThrows(ObjectNotFound.class, () -> userService.findByNameStartingWithIgnoreCase("z"));
		assertEquals("Nenhum nome de usuário inicia com z cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir usuario")
	void insertUserTest() {
		User usuario = new User(null, "insert", "insert", "insert", "ADMIN");
		userService.insert(usuario);
		usuario = userService.findById(usuario.getId());
		assertEquals(1, usuario.getId());
		assertEquals("insert", usuario.getName());
		assertEquals("insert", usuario.getEmail());
		assertEquals("insert", usuario.getPassword());
	}

	@Test
	@DisplayName("Teste apagar usuario")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void deleteByIdTest() {
		userService.delete(3);
		List<User> list = userService.listAll();
		assertEquals(1, list.size());
	}

	@Test
	@DisplayName("Teste apagar usuario inexistente")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void deleteByIdNonExistsTest() {
		var exception = assertThrows(ObjectNotFound.class, () -> userService.delete(10));
		assertEquals("O usuário 10 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste alterar usuario inexistente")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void updateUserNonExistsTest() {
		User usuario = new User(20, "update", "update", "update", "ADMIN");
		var exception = assertThrows(ObjectNotFound.class, () -> userService.update(usuario));
		assertEquals("O usuário 20 não existe", exception.getMessage());
	}

	@Test
	@DisplayName("Teste listar todos sem usuarios cadastrados")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void listAllNonExistsUserTest() {
		userService.delete(3);
		userService.delete(4);
		var exception = assertThrows(ObjectNotFound.class, () -> userService.listAll());
		assertEquals("Nenhum usuário cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste inserir usuario com email duplicado")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void insertUserEmailDuplicateTest() {
		User usuario = new User(null, "insert", "Email 1", "insert", "ADMIN");
		var exception = assertThrows(IntegrityViolation.class, () -> userService.insert(usuario));
		assertEquals("Email já existente: Email 1", exception.getMessage());

	}

	@Test
	@DisplayName("Teste alterar usuario com email duplicado")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void updateUserEmailWrongTest() {
		User usuario = new User(4, "update", "Email 1", "update", "ADMIN");
		var exception = assertThrows(IntegrityViolation.class, () -> userService.update(usuario));
		assertEquals("Email já existente: Email 1", exception.getMessage());
	}

	@Test
	@DisplayName("Teste alterar usuario")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void updateUserTest() {
		User usuario = new User(3, "update", "update", "update", "ADMIN");
		userService.update(usuario);
		assertEquals(3, usuario.getId());
		assertEquals("update", usuario.getName());
		assertEquals("update", usuario.getEmail());
		assertEquals("update", usuario.getPassword());
	}

	@Test
	@DisplayName("Teste listar todos")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void listAllTest() {
		List<User> lista = userService.listAll();
		assertEquals(2, lista.size());
	}

	@Test
	@DisplayName("Teste buscar usuario com ignore case")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void findByNameIgnoreCaseWrongTest() {
		var lista = userService.findByNameIgnoreCase("USER 1");
		assertEquals(1, lista.size());
		var exception = assertThrows(ObjectNotFound.class, () -> userService.findByNameIgnoreCase("UZER 1"));
		assertEquals("Nenhum nome de usuário UZER 1 cadastrado", exception.getMessage());
	}

	@Test
	@DisplayName("Teste buscar usuário por email")
	@Sql({ "classpath:/resources/sqls/usuario.sql" })
	void findByEmailTest() {
		Optional<User> user = userService.findByEmail("Email 1");
		assertEquals("Email 1", user.get().getEmail());
		var exception = assertThrows(ObjectNotFound.class, () -> userService.findByEmail("Email 0909"));
		assertEquals("Nenhum email de usuário Email 0909 cadastrado", exception.getMessage());
	}

}
