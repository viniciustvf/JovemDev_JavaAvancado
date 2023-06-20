package br.com.trier.springvespertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springvespertino.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByNameStartingWithIgnoreCase(String name);
	
	List<User> findByNameIgnoreCase(String name);
	
	User findByEmail(String email);
	
}
