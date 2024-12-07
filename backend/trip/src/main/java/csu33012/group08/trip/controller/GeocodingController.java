package csu33012.group08.trip.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import csu33012.group08.trip.service.GeocodingService;

@RestController
public class GeocodingController {

    private final GeocodingService geocodingService;

    public GeocodingController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping("/getcoordinates")
    public ResponseEntity<?> getCoordinates(@RequestParam String address) {
        double[] latlgn = geocodingService.getLatLng(address);

        if (latlgn == null) {
            return ResponseEntity.badRequest().body("Invalid Address");
        }

        return ResponseEntity.ok(latlgn);
    }
}
