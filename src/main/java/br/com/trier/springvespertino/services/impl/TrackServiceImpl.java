package br.com.trier.springvespertino.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Track;
import br.com.trier.springvespertino.repositories.TrackRepository;
import br.com.trier.springvespertino.services.TrackService;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;

@Service
public class TrackServiceImpl implements TrackService {

	@Autowired
	TrackRepository repository;

	private void validateTrack(Track track) {
		if (track.getSize() == null || track.getSize() <= 0) {
			throw new IntegrityViolation("Tamanho da pista inválido");
		}
	}

	@Override
	public Track findById(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFound("A pista %s não existe".formatted(id)));
	}

	@Override
	public Track insert(Track track) {
		validateTrack(track);
		return repository.save(track);
	}

	@Override
	public Track update(Track track) {
		validateTrack(track);
		return repository.save(track);
	}

	@Override
	public void delete(Integer id) {
		Track track = findById(id);
		repository.delete(track);
	}

	@Override
	public List<Track> listAll() {
		List<Track> lista = repository.findAll();
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada");
		}
		return lista;
	}

	@Override
	public List<Track> findByNameStartingWithIgnoreCase(String name) {
		List<Track> lista = repository.findByNameStartingWithIgnoreCase(name);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada com %s".formatted(name));
		}
		return lista;
	}

	@Override
	public List<Track> findBySizeBetween(Integer sizeIn, Integer sizeFin) {
		List<Track> lista = repository.findBySizeBetween(sizeIn, sizeFin);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada entre %s e %s metros".formatted(sizeIn, sizeFin));
		}
		return lista;
	}

	@Override
	public List<Track> findByCountryOrderBySizeDesc(Country country) {
		List<Track> lista = repository.findByCountryOrderBySizeDesc(country);
		if (lista.isEmpty()) {
			throw new ObjectNotFound("Nenhuma pista cadastrada no país: %s".formatted(country.getName()));
		}
		return lista;
	}
}
