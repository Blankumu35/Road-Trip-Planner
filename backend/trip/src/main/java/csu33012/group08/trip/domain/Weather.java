package csu33012.group08.trip.domain;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="weather")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String address;

    @Column
    private String weather;

    @Column
    private String description;

    @Column
    private String icon;

    @Column
    private Double temperature;

    @Column
    private Double humidity;

    @Column
    private Double windspeed;

    public Weather() {}

    public Weather(String address, String weather, String description, String icon, Double temperature, Double humidity, Double windspeed) {
        this.address = address;
        this.weather = weather;
        this.description = description;
        this.icon = icon;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windspeed = windspeed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(Double windspeed) {
        this.windspeed = windspeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather1 = (Weather) o;
        return Double.compare(weather1.temperature, temperature) == 0 &&
        Double.compare(weather1.humidity, humidity) == 0 &&
        Double.compare(weather1.windspeed, windspeed) == 0 &&
                Objects.equals(weather, weather1.weather);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weather, temperature, humidity, windspeed);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}