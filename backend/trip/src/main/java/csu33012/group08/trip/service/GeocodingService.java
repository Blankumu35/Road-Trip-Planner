package csu33012.group08.trip.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Service
public class GeocodingService {
    private static final Logger logger = LoggerFactory.getLogger(GeocodingService.class);

    @Value("${google.api.key}")
    private String apiKey;

    public GeocodingService() {

    }

    public double[] getLatLng(String address) {
        try {
            GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();
            logger.debug("Getting address: " + address);

            GeocodingResult[] response = GeocodingApi.geocode(context, address).await();

            // Invoke .shutdown() after the application is done making requests
            context.shutdown();

            double lat = response[0].geometry.location.lat;
            double lng = response[0].geometry.location.lng;

            logger.debug("Recieved Latitude" + lat);
            logger.debug("Recieved Longitude:" + lng);

            // Round to 3 decimal places
            lat = Math.round(lat * Math.pow(10, 3)) / Math.pow(10, 3);
            lng = Math.round(lng * Math.pow(10, 3)) / Math.pow(10, 3);

            double[] latlgn = {lat, lng};
                
            return latlgn;
        } catch (ApiException e) {
            logger.error("ApiException at GeocodingService.java: " + e.toString(), e);
        } catch (InterruptedException e) {
            logger.error("InterruptedException at GeocodingService.java: " + e.toString(), e);
        } catch (IOException e) {
            logger.error("IOException at GeocodingService.java: " + e.toString(), e);
        } catch (Exception e) {
            logger.error("Exception at GeocodingService.java: " + e.toString(), e);
        }

        return null;
    }
}
