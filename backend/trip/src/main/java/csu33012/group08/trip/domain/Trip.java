package csu33012.group08.trip.domain;

import jakarta.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @Column(name = "id")
    private String tripName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "trip_weather_mapping", 
      joinColumns = {@JoinColumn(name = "trip_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "weather_id", referencedColumnName = "id")})
    @MapKey(name = "address")
    private Map<String, Weather> locations = new LinkedHashMap<>();    

    public Trip() {}

    public Trip(String tripName, String address, Weather w) {
        this.tripName = tripName;
        this.locations.put(address, w);
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public Map<String, Weather> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, Weather> locations) {
        this.locations = locations;
    }

    public void addLocation(String address, Weather weather) {
        this.locations.put(address, weather);
    }    
}
