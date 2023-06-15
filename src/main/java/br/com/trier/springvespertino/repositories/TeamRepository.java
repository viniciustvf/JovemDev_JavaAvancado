package br.com.trier.springvespertino.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.trier.springvespertino.models.Team;

public interface TeamRepository extends JpaRepository<Team, Integer>{

}
