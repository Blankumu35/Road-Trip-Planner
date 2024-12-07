package csu33012.group08.trip.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import csu33012.group08.trip.service.OptimizerService;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class OptimizerController {
    private final OptimizerService optimizerService;

    public OptimizerController(OptimizerService optimizerService) {
        this.optimizerService = optimizerService;
    }

    @GetMapping("/getroute")
    public ResponseEntity<?> getWeatherByAddress(@RequestParam String[] address) {
        Map optimizedRoute = optimizerService.getOptimizedRouteByAddress(address);

        if (!optimizedRoute.containsKey("route")) {
            return ResponseEntity.badRequest().body("Invalid Address");
        } else if ( optimizedRoute.get("route") == (null)){
            return ResponseEntity.badRequest().body("Invalid Address");
        }
        
        return ResponseEntity.ok(optimizedRoute);
    }
}
