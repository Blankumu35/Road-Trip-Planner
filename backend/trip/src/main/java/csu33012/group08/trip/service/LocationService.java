package csu33012.group08.trip.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csu33012.group08.trip.domain.Trip;
import csu33012.group08.trip.domain.Weather;
import csu33012.group08.trip.repo.TripRepository;

@Service
public class LocationService {

    @Autowired TripRepository tripRepository;

    @Autowired WeatherService weatherService;

    public void newTrip(String tripName, String address) {
        Weather w = new Weather(address, "", "", "", 0.0, 0.0, 0.0);
        Trip t = new Trip(tripName,address,w);
        tripRepository.save(t);
    }

    public List<String> getTrip(){
        List<String> trips = new ArrayList<>();
        for (Trip t:tripRepository.findAll()) {
            trips.add(t.getTripName());
        }
        return trips;
    }
    
    public void newLoc(String address, String tripName) {
        if (address.equals("Thisisadummylocationsothaticantest")) {
            Weather w = new Weather("NeverLand","Sunny","It's sunny bro", "", 1.0,2.0,3.0);
            Trip t = new Trip("PeterPan","NeverLand",w);
            tripRepository.save(t);
        } else {
            Weather weather = parseWeather(address);
            Optional<Trip> tripOptional = tripRepository.findById(tripName);
            Trip trip;
            if(tripOptional.isPresent()) {
                trip = tripOptional.get();
                trip.addLocation(address, weather);
            } else {
                trip = new Trip(tripName, address, weather);
            }
            tripRepository.save(trip);
        }
    }

    public Map<String, Weather> allLocs(String tripName) {
        Optional<Trip> tripOptional = tripRepository.findById(tripName);
        if(tripOptional.isPresent()) {
            Trip trip = tripOptional.get();
            Map<String, Weather> locs = trip.getLocations();
            return locs;
        } else return null;
    }

    public String deleteLoc(String tripName, String address) {
        Optional<Trip> tripOptional = tripRepository.findById(tripName);
        if(tripOptional.isPresent()) {
            Trip trip = tripOptional.get();
            Map<String, Weather> locs = trip.getLocations();
            if (locs.containsKey(address)) {
                locs.remove(address);
                return "ok";
            } else return "no loc";
        } else return "no trip";
    }

    public String deleteTrip(String tripName) {
        tripRepository.deleteById(tripName);
        return "deleted";
    }

    public Weather parseWeather(String address) {
        Map<String,Object> weather = weatherService.getWeatherByAddress(address);
        Weather w = new Weather();
        w.setAddress(address);
        w.setWeather((String)weather.get("weather"));
        w.setDescription((String)weather.get("description"));
        w.setIcon((String)weather.get("icon"));
        w.setTemperature( (Double)weather.get("temperature") );
        w.setHumidity( (Double)weather.get("humidity") );
        w.setWindspeed( (Double)weather.get("wind_speed") );
        return w;
    }
}
