package br.com.trier.springvespertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Team;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

	List<Country> findByNameStartingWithIgnoreCase(String name);

	Country findByNameIgnoreCase(String name);

}
