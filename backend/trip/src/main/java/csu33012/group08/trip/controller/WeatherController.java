package csu33012.group08.trip.controller;

import csu33012.group08.trip.service.WeatherService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/getweather")
    public ResponseEntity<?> getWeatherByAddress(@RequestParam String address) {
        Map<String, Object> weather = weatherService.getWeatherByAddress(address);

        if (weather == null) {
            return ResponseEntity.badRequest().body("Invalid Address");
        }

        return ResponseEntity.ok(weather);
    }
}
