package br.com.trier.springvespertino.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Integer> {

	List<Track> findByNameStartingWithIgnoreCase(String name);

	List<Track> findBySizeBetween(Integer sizeIn, Integer sizeFin);

	List<Track> findByCountryOrderBySizeDesc(Country country);

}