package csu33012.group08.trip.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WeatherControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetWeatherByValidAddress() {
        String address = "College Green, Dublin 2, Ireland";

        ResponseEntity<Map> response = restTemplate.getForEntity(
            "/getweather?address=" + address, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, Object> responseBody = response.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody).containsKeys("weather", "description", "icon", 
            "temperature", "humidity", "wind_speed", "name");
    }

    @Test
    public void testGetWeatherByValidAddressTwo() {
        String address = "Glendalough, Co. Wicklow, Ireland";

        ResponseEntity<Map> response = restTemplate.getForEntity(
            "/getweather?address=" + address, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, Object> responseBody = response.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody).containsKeys("weather", "description", "icon", 
            "temperature", "humidity", "wind_speed", "name");
    }

    @Test
    public void testGetWeatherByValidAddressThree() {
        String address = "Silverstone Circuit, Towcester NN12 8TN, United Kingdom";

        ResponseEntity<Map> response = restTemplate.getForEntity(
            "/getweather?address=" + address, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, Object> responseBody = response.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody).containsKeys("weather", "description", "icon", 
            "temperature", "humidity", "wind_speed", "name");
    }

    @Test
    public void testGetWeatherByValidAddressFour() {
        String address = "Niagara Falls, Canada";

        ResponseEntity<Map> response = restTemplate.getForEntity(
            "/getweather?address=" + address, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, Object> responseBody = response.getBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody).containsKeys("weather", "description", "icon", 
            "temperature", "humidity", "wind_speed", "name");
    }

    @Test
    public void testGetWeatherByInvalidAddress() {
        String address = "This is an invalid address";

        ResponseEntity<String> response = restTemplate.getForEntity(
            "/getweather?address=" + address, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid Address");
    }
}
