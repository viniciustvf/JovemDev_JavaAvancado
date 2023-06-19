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
import br.com.trier.springvespertino.models.Country;
import jakarta.transaction.Transactional;

@Transactional
public class CountryServiceTest extends BaseTests {

	@Autowired
	CountryService countryService;
	
	@Test
    @DisplayName("Teste buscar pais por ID")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByIdTest() {
        var country = countryService.findById(1);
        assertNotNull(country);
        assertEquals(1, country.getId());
        assertEquals("Pais 1", country.getName());
    }
	
	@Test
    @DisplayName("Teste buscar pais por ID inexistente")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByIdWrongTest() {
        var country = countryService.findById(10);
        assertNull(country);
    }
	
	@Test
    @DisplayName("Teste buscar pais por nome")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByNameTest() {
        var country = countryService.findByName("Pais 1");
        assertNotNull(country);
        assertEquals(1, country.size());
    }
	
	@Test
    @DisplayName("Teste buscar pais por nome inexistente")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByNameWrongTest() {
        var pais = countryService.findByName("z");
        assertEquals(0, pais.size());
    }
	
	@Test
    @DisplayName("Teste buscar pais por letra que começa")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByNameStartingWithTest() {
        var list = countryService.findByNameStartingWithIgnoreCase("p");
        assertNotNull(list);
        assertEquals(2, list.size());
    }
	
	@Test
    @DisplayName("Teste buscar pais por letra que começa inexistente")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByNameStartingWithWrongTest() {
        var list = countryService.findByNameStartingWithIgnoreCase("z");
        assertEquals(0, list.size());
    }
	
	@Test
    @DisplayName("Teste inserir pais")
    void insertCountryTest() {
        Country country = new Country(null, "insert");
        countryService.insert(country);
        country = countryService.findById(1);
        assertEquals(1, country.getId());
        assertEquals("insert", country.getName());
    }
	
	@Test
    @DisplayName("Teste apagar pais por ID")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void deleteByIdTest() {
        countryService.delete(1);
        List<Country> list = countryService.listAll();
        assertEquals(1, list.size());
    }
	
	@Test
    @DisplayName("Teste apagar pais por ID incorreto")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void deleteByIdNonExistsTest() {
        countryService.delete(10);
        List<Country> list = countryService.listAll();
        assertEquals(2, list.size());
    }
	
	@Test
    @DisplayName("Teste alterar pais")
    @Sql({"classpath:/resources/sqls/usuario.sql"})
    void updateByIdTest() {
    	Country country = new Country(1, "update");
    	countryService.update(country);
    	assertEquals("update", country.getName());
    	assertEquals(1, country.getId());
    }
}
