package csu33012.group08.trip.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import csu33012.group08.trip.domain.Weather;
import csu33012.group08.trip.service.LocationService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class TripController {

    @Autowired LocationService locationService;

    @PostMapping("/{tripName}")
    public ResponseEntity<String> createLocation(
        @PathVariable String tripName,
        @RequestBody String address) {
        locationService.newLoc(address, tripName);
        return ResponseEntity.ok(address);
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAll(){
        return ResponseEntity.ok(locationService.getTrip());
    }

    @GetMapping("/{tripName}")
    public ResponseEntity<Map<String,Weather>> getLocations(@PathVariable String tripName) {
        Map<String,Weather> locs = locationService.allLocs(tripName);
        if (locs == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (locs.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else return ResponseEntity.ok(locs);
    }

    @DeleteMapping("/{tripName}")
    public ResponseEntity<String> deleteLocation(
        @PathVariable String tripName, 
        @RequestBody String address) {
        String response = locationService.deleteLoc(tripName, address);
        if (response.equalsIgnoreCase("ok")) {
            return ResponseEntity.ok("Location Deleted");
        } else if (response.equalsIgnoreCase("no loc")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trip does not contain location");
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip name not found");
    }

    @DeleteMapping("/delete/{tripName}")
    public ResponseEntity<String> deleteTrip(
        @PathVariable String tripName) {
        locationService.deleteTrip(tripName);
        return ResponseEntity.ok("Trip Deleted");
    }

    @PostMapping("/trip/{tripName}")
    public ResponseEntity<List<String>> createLocations(
        @PathVariable String tripName,
        @RequestBody List<String> addresses) {
            for (String addr : addresses){
                locationService.newLoc(addr, tripName);
            }
            return ResponseEntity.ok(addresses);
    }
}
