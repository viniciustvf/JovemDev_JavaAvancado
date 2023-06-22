package br.com.trier.springvespertino;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import br.com.trier.springvespertino.services.ChampionshipService;
import br.com.trier.springvespertino.services.CountryService;
import br.com.trier.springvespertino.services.PilotService;
import br.com.trier.springvespertino.services.RaceService;
import br.com.trier.springvespertino.services.TeamService;
import br.com.trier.springvespertino.services.TrackService;
import br.com.trier.springvespertino.services.UserService;
import br.com.trier.springvespertino.services.impl.ChampionshipServiceImpl;
import br.com.trier.springvespertino.services.impl.CountryServiceImpl;
import br.com.trier.springvespertino.services.impl.PilotServiceImpl;
import br.com.trier.springvespertino.services.impl.RaceServiceImpl;
import br.com.trier.springvespertino.services.impl.TeamServiceImpl;
import br.com.trier.springvespertino.services.impl.TrackServiceImpl;
import br.com.trier.springvespertino.services.impl.UserServiceImpl;

@TestConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class BaseTests {

	@Bean
	public UserService userService() {
		return new UserServiceImpl();
	}
	
	@Bean
	public TeamService TeamService() {
		return new TeamServiceImpl();
	}
	
	@Bean
	public CountryService CountryService() {
		return new CountryServiceImpl();
	}
	
	@Bean
	public ChampionshipService ChampionshipService() {
		return new ChampionshipServiceImpl();
	}
	
	@Bean
	public TrackService TrackService() {
		return new TrackServiceImpl();
	}
	
	@Bean
	public PilotService PilotService() {
		return new PilotServiceImpl();
	}
	
	@Bean
	public RaceService RaceService() {
		return new RaceServiceImpl();
	}
	
	
}
