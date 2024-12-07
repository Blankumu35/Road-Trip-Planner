package csu33012.group08.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import csu33012.group08.trip.service.LocationService;

@SpringBootApplication
public class TripApplication implements CommandLineRunner{

	@Autowired
	LocationService locationService;

	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

	public static void main(String[] args) {
		SpringApplication.run(TripApplication.class, args);
	}

	@Override
    public void run(String... args) throws Exception {
        // Initialize the database with some items
        locationService.newTrip("DummyTrip","DummyAddress");
		// locationService.newLoc("address", "tripName");
    }
}
