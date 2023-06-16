package br.com.trier.springvespertino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springvespertino.models.Race;

@Repository
public interface RaceRepository extends JpaRepository<Race, Integer>{

}
