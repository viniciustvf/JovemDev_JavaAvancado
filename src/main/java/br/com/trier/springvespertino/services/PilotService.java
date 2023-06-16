package br.com.trier.springvespertino.services;

import java.util.List;

import br.com.trier.springvespertino.models.Pilot;

public interface PilotService {

	Pilot findById(Integer id);
	
	Pilot insert(Pilot team);
	
	List<Pilot> listAll();
	
	Pilot update (Pilot team);
	
	void delete (Integer id);
	
}
