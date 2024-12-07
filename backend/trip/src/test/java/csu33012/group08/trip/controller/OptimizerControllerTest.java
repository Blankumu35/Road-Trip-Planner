package csu33012.group08.trip.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OptimizerControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetRoute() {
        String[] addresses = { 
            "Dublin, Ireland",
            "Lucan, County Dublin, Ireland",
            "Cork City, County Cork, Ireland",
            "Galway, Ireland",
            "Dingle, County Kerry, Ireland"
        };
       
        String query = String.join("&address=", addresses); // Join addresses as a query string

        String url = "/getroute?address=" + query;
        ResponseEntity<HashMap> response = restTemplate.getForEntity(url, HashMap.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        Map<String, List> optimizedRoute = response.getBody();
        assertThat(optimizedRoute).isNotNull();
        assertThat(optimizedRoute.get("route")).isInstanceOfAny(List.class);
        
        List route = optimizedRoute.get("route");
        assertThat(route).hasSize(5);
        ArrayList<Integer> expected = new ArrayList<>(List.of(0,1,3,2,4));
        assertThat(route).isEqualTo(expected);

    }
    

    @Test
    public void testGetRouteInvalidAddress() {
        String[] addresses = { 
            "Dublin",
            "fakeDublin",
            "fakeMayo",
        };
       
        String query = String.join("&address=", addresses);

        String url = "/getroute?address=" + query;
        ResponseEntity<?> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid Address");
    }
}
