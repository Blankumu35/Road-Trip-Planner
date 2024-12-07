package csu33012.group08.trip.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;


import org.springframework.http.HttpMethod;
@Service
public class OptimizerService {
    private static final Logger logger = LoggerFactory.getLogger(OptimizerService.class);

    private final RestTemplate restTemplate;

    @Value("${mapquest.api.key}")
    private String apiKey;

    public OptimizerService() {
        this.restTemplate = new RestTemplate();
    }

    
    public Map getOptimizedRouteByAddress(String[] addresses) {
        logger.debug("Optimizing for addresses: " + addresses);

          String MAPQUEST_API_URL = "https://www.mapquestapi.com/directions/v2/optimizedroute?key=" + apiKey;

          Map<String, Object> requestBody = Map.of(
              "locations", addresses
          );
      
          HttpHeaders headers = new HttpHeaders();
              headers.set("Content-Type", "application/json");
      
          HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
      
          RestTemplate restTemplate = new RestTemplate();
          ResponseEntity<Map> response = restTemplate.exchange(
              MAPQUEST_API_URL,
              HttpMethod.POST,
              entity,
              Map.class
          );
      
          if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> data = response.getBody();
            Map<String, Object> route = (Map<String, Object>) data.get("route");
            Map<String, Object> optimizedRoute = new HashMap();
            optimizedRoute.put("route", route.get("locationSequence"));
            return optimizedRoute;
          } else {
              throw new RuntimeException("Failed to fetch optimized route: " + response.getStatusCode());
          }        
    }
}
