package csu33012.group08.trip.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(GeocodingService.class);

    private final RestTemplate restTemplate;

    @Value("${openweather.api-key}")
    private String apiKey;

    @Value("${openweather.base-url}")
    private String baseUrl;

    @Autowired
    private GeocodingService geocodingService;

    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> getWeatherByAddress(String address) {
        logger.debug("Getting geocoding co-ordinates for address: " + address);
        double[] latlgn = geocodingService.getLatLng(address);

        if (latlgn == null) {
            return null;
        }

        return getWeatherByCoordinates(latlgn[0], latlgn[1]);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getWeatherByCoordinates(double latitude, double longitude) {
        logger.debug("Getting weather for co-ordinates: " + latitude + ", " + longitude);

        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/weather")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("apiKey", apiKey)
                .build()
                .toUri();
            
            Map<String, Object> weatherMap = restTemplate.getForObject(uri, Map.class);
            Map<String, Object> newWeatherMap = new HashMap<>();

            logger.debug("Extracted weather data from API:" + weatherMap.toString());

            // Extract "weather.main", "weather.description" and "weather.icon"
            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) weatherMap.get("weather");
            
            if (weatherList != null && !weatherList.isEmpty()) {
                Map<String, Object> weather = weatherList.get(0);

                if (weather.get("main") != null) {
                    logger.debug("Key: \"weather\" Value: " + weather.get("main"));
                    newWeatherMap.put("weather", weather.get("main"));
                }

                if (weather.get("description") != null) {
                    logger.debug("Key: \"description\" Value: " + weather.get("description"));
                    newWeatherMap.put("description", weather.get("description"));
                }

                if (weather.get("icon") != null) {
                    logger.debug("Key: \"icon\" Value: " + weather.get("icon"));
                    newWeatherMap.put("icon", weather.get("icon"));
                }
            }

            // Extract "main.temp" and "main.humidity"
            Map<String, Object> main = (Map<String, Object>) weatherMap.get("main");
            
            if (main != null) {
                if (main.get("temp") != null) {
                    Object tempObject = weatherMap.get("temp");
                    logger.debug("Key: \"temperature\" Value: " + tempObject);

                    if (tempObject instanceof Integer) {
                        int tempKelvin = (Integer) tempObject;
                        double tempCelcius = tempKelvin - 273.15;
                        tempCelcius = Math.round(tempCelcius * Math.pow(10, 2)) / Math.pow(10, 2);
                        weatherMap.put("temperature", tempCelcius);
                    }
                    else {
                        double tempKelvin = (double) main.get("temp");
                        double tempCelcius = tempKelvin - 273.15;
                        tempCelcius = Math.round(tempCelcius * Math.pow(10, 2)) / Math.pow(10, 2);
                        newWeatherMap.put("temperature", tempCelcius);
                    }
                }

                if (main.get("humidity") != null) {
                    logger.debug("Key: \"humidity\" Value: " + main.get("humidity"));

                    if (main.get("humidity") instanceof Integer) {
                        int tempHumidity = (Integer) main.get("humidity");
                        double humidity = tempHumidity;
                        newWeatherMap.put("humidity", humidity);
                    }
                    else {
                        double humidity = (double) main.get("humidity");
                        newWeatherMap.put("humidity", humidity);
                    }
                }
            }

            // Extract "wind.speed"
            Map<String, Object> wind = (Map<String, Object>) weatherMap.get("wind");
            
            if (wind != null && wind.get("speed") != null) {
                logger.debug("Key: \"wind_speed\" Value: " + wind.get("speed"));
                
                if (wind.get("speed")instanceof Integer) {
                    int tempWind= (Integer) wind.get("speed");
                    double doubleWind = tempWind;
                    newWeatherMap.put("wind_speed", doubleWind);
                }
                else {
                    newWeatherMap.put("wind_speed",  (double) wind.get("speed"));
                }
            }

            // Extract "name"
            String name = (String) weatherMap.get("name");

            if (name != null) {
                newWeatherMap.put("name", name);
            }

            logger.debug("Filtered weather data from API:" + newWeatherMap.toString());

            return newWeatherMap;
        } catch (Exception e) {
            logger.error("Exception at WeatherService.java", e);
        }

        return null;
    }
}
