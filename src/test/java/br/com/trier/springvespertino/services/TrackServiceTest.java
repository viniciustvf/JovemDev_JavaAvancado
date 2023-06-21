package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTests;
import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Track;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import jakarta.transaction.Transactional;

@Transactional
public class TrackServiceTest extends BaseTests {
	
	@Autowired
	TrackService trackService;
	
	@Autowired
	CountryService countryService;
	
	@Test
    @DisplayName("Teste buscar pista por ID")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
	void findByIdTest() {
        var track = trackService.findById(1);
        assertNotNull(track);
        assertEquals(1, track.getId());
        assertEquals("Pista 1", track.getName());
        assertEquals(1, track.getCountry().getId());
        assertEquals(4000, track.getSize());
    }
    
    @Test
    @DisplayName("Teste buscar pista por ID inexistente")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
    void findByIdWrongTest() {
        var exception = assertThrows(ObjectNotFound.class,() -> trackService.findById(10));
        assertEquals("A pista 10 não existe", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste inserir pista")
    void insertTrackTest() {
    	Country country = countryService.insert(new Country(3, "País 3"));
    	Track pista = new Track(null, "monaco", 6000, country);
        trackService.insert(pista);
        pista = trackService.findById(pista.getId());
        assertEquals(1, pista.getId());
        assertEquals("monaco", pista.getName());
        assertEquals(6000, pista.getSize());
        assertEquals(1, pista.getCountry().getId());
        assertEquals("País 3", pista.getCountry().getName());
        Track pista2 = new Track(null, "monaco", 0, country);
        var exception2 = assertThrows(IntegrityViolation.class,() -> trackService.insert(pista2));
        assertEquals("Tamanho da pista inválido", exception2.getMessage());
    }
    
    @Test
    @DisplayName("Teste listar todos")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
    void listAllTest() {
    	List<Track> lista = trackService.listAll();
    	assertEquals(2, lista.size());
    }
    
    @Test
    @DisplayName("Teste listar todos sem pistas cadastradas")
    void listAllNonExistsTrackTest() {
    	var exception = assertThrows(ObjectNotFound.class,() -> trackService.listAll());
        assertEquals("Nenhuma pista cadastrada", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste alterar pista")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    @Sql({"classpath:/resources/sqls/pista.sql"})
    void updateTrackTest() {
    	Country country = countryService.insert(new Country(3, "País 3"));
    	Track pista = new Track(1, "dinamarca", 6000, country);
        trackService.update(pista);
        assertEquals(1, pista.getId());
        assertEquals("dinamarca", pista.getName());
        assertEquals(6000, pista.getSize());
        assertEquals(3, pista.getCountry().getId());
        assertEquals("País 3", pista.getCountry().getName());
    }
    
    @Test
    @DisplayName("Teste apagar pista")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
    void deleteByIdTest() {
        trackService.delete(1);
        List<Track> list = trackService.listAll();
        assertEquals(1, list.size());
        var exception = assertThrows(ObjectNotFound.class,() -> trackService.delete(10));
        assertEquals("A pista 10 não existe", exception.getMessage()); 
    }
    
    @Test
    @DisplayName("Teste buscar pista com ignore case")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
    void findByNameIgnoreCaseWrongTest() {
        var lista = trackService.findByNameStartingWithIgnoreCase("p");
        assertEquals(2, lista.size());
        var exception = assertThrows(ObjectNotFound.class, () -> trackService.findByNameStartingWithIgnoreCase("z"));
        assertEquals("Nenhuma pista cadastrada com z", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar pista por tamanho between")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
    void findByYearBetweenTest() {
        var lista = trackService.findBySizeBetween(2000, 6000);
        assertEquals(2, lista.size());
        var exception = assertThrows(ObjectNotFound.class, () -> trackService.findBySizeBetween(500, 600));
        assertEquals("Nenhuma pista cadastrada entre 500 e 600 metros", exception.getMessage());
    }
    
    @Test
    @DisplayName("Teste buscar pista por tamanho between")
	@Sql({"classpath:/resources/sqls/pais.sql"})
	@Sql({"classpath:/resources/sqls/pista.sql"})
    void findByCountryOrderBySizeDescTest() {
    	Track track = trackService.findById(1);
    	var lista = trackService.findByCountryOrderBySizeDesc(track.getCountry());
        assertEquals(1, lista.size()); 
        Country country = new Country(3, "País 3");
        var exception = assertThrows(ObjectNotFound.class, () -> trackService.findByCountryOrderBySizeDesc(country));
        assertEquals("Nenhuma pista cadastrada no país: País 3", exception.getMessage());
    }

} 
