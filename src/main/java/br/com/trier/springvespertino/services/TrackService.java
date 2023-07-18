package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Track;

public interface TrackService {

	Track findById(Integer id);

	Track insert(Track track);

	List<Track> listAll();

	Track update(Track track);

	void delete(Integer id);

	List<Track> findByNameStartingWithIgnoreCase(String name);

	List<Track> findBySizeBetween(Integer sizeIn, Integer sizeFin);

	List<Track> findByCountryOrderBySizeDesc(Country country);

}