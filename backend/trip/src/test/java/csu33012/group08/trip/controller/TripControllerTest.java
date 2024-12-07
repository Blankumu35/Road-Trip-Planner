package csu33012.group08.trip.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;

import csu33012.group08.trip.domain.Trip;
import csu33012.group08.trip.domain.Weather;
import csu33012.group08.trip.repo.TripRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TripControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TripRepository tripRepository;

    @BeforeEach
    public void setup() {
        // Clear the database before each test
        tripRepository.deleteAll();

        String l1 = "Dublin";
        String l2 = "Mullingar";
        String l3 = "Galway";
        Weather w0 = new Weather("Kerry","Rain","","",1.0,2.0,3.0);
        Weather w1 = new Weather(l1,"Rain","","", 1.0,2.0,3.0);
        Weather w2 = new Weather(l2,"Sun","","", 1.0,2.0,3.0);
        Weather w3 = new Weather(l3,"Cloud","","", 1.0, 2.0, 3.0);
        Trip trip1 = new Trip("trip1", "Kerry", w0);

        // Add locations to trips
        trip1.addLocation(l1, w1);
        trip1.addLocation(l2, w2);
        trip1.addLocation(l3, w3);

        tripRepository.save(trip1);
    }

    @Test
    public void checkRepo() {
        // Test retrieving locations for trip1
        ResponseEntity<Map<String, Weather>> responseEntity = restTemplate.exchange(
            "/trip1", HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Weather>>() {}
        );

        // Ensure the repository has the trips saved
        List<Trip> trips = tripRepository.findAll();
        assertThat(trips).hasSize(1); // We should have 1 trip saved

        // Check that the request was successful
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();

        // Get the response body (the map of locations and their weather)
        Map<String, Weather> locations = responseEntity.getBody();
        assertThat(locations).isNotNull();
        assertThat(locations.size()).isEqualTo(4); // There should be 4 locations for trip1

        Weather w = locations.get("Mullingar");
        assertThat(w.getAddress()).isEqualTo("Mullingar");
        assertThat(w.getWeather()).isEqualTo("Sun");
        assertThat(w.getTemperature()).isEqualTo(1);
        assertThat(w.getHumidity()).isEqualTo(2);
        assertThat(w.getWindspeed()).isEqualTo(3);
    }

    @Test
    public void checkPost() {
        // Test retrieving locations for trip1
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/trip2", "Thisisadummylocationsothaticantest", String.class);

        // Ensure the repository has the trips saved
        List<Trip> trips = tripRepository.findAll();
        assertThat(trips).hasSize(2); // We should have 2 trips saved

        // Check that the request was successful
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();

        // Get the response body (the map of locations and their weather)
        String locations = responseEntity.getBody();
        assertThat(locations).isNotNull();
        assertThat(locations).isEqualTo("Thisisadummylocationsothaticantest"); // There should be 3 locations for trip1
    }

    @Test
    public void checkGet() {
        Weather w = new Weather();
        Trip t = new Trip("trip2", "t21", w);
        tripRepository.save(t);
        ResponseEntity<String[]> responseEntity = restTemplate.getForEntity("/all", String[].class);
        
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();

        String[] r = responseEntity.getBody();
        assertThat(r).isNotNull();
        assertThat(r[1]).isEqualTo("trip2");
    }
}
