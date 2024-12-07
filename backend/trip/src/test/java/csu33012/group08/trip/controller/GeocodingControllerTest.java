package csu33012.group08.trip.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GeocodingControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetCoordinatesValidAddress() {
        String address = "College Green, Dublin 2, Ireland";
        double[] expectedCoordinates = {53.344, -6.261};

        ResponseEntity<double[]> response = restTemplate.getForEntity(
            "/getcoordinates?address=" + address, double[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedCoordinates);
    }

    @Test
    public void testGetCoordinatesValidAddressTwo() {
        String address = "Glendalough, Co. Wicklow, Ireland";
        double[] expectedCoordinates = {53.011, -6.326};

        ResponseEntity<double[]> response = restTemplate.getForEntity(
            "/getcoordinates?address=" + address, double[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedCoordinates);
    }

    @Test
    public void testGetCoordinatesValidAddressThree() {
        String address = "Silverstone Circuit, Towcester NN12 8TN, United Kingdom";
        double[] expectedCoordinates = {52.074, -1.022};

        ResponseEntity<double[]> response = restTemplate.getForEntity(
            "/getcoordinates?address=" + address, double[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedCoordinates);
    }

    @Test
    public void testGetCoordinatesValidAddressFour() {
        String address = "Niagara Falls, Canada";
        double[] expectedCoordinates = {43.09, -79.085};

        ResponseEntity<double[]> response = restTemplate.getForEntity(
            "/getcoordinates?address=" + address, double[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedCoordinates);
    }

    @Test
    public void testGetCoordinatesInvalidAddress() {
        String invalidAddress = "This is an invalid address";

        ResponseEntity<String> response = restTemplate.getForEntity(
            "/getcoordinates?address=" + invalidAddress, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid Address");
    }
}
