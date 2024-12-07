package csu33012.group08.trip.controller;

import csu33012.group08.trip.dto.ChatRequest;
import csu33012.group08.trip.dto.ChatResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt) {
        logger.debug("Generating itinerary...");
        String context = "Your are a travel agent helping to plan a roadtrip. Make an itinerary for the following places in order: " + prompt + " Format your response as an array of objects with the following keys: 'days' the number of days and location, 'activities' an array of string where each string is a suggested activity with details. Make sure everything is formatted as proper JSON";

        try {
            ChatRequest request = new ChatRequest(model, context);
            ChatResponse chatGptResponse = template.postForObject(apiURL, request, ChatResponse.class);
            String response = chatGptResponse.getChoices().get(0).getMessage().getContent();
            logger.debug("Generated itinerary: " + response);
            return response;
        } catch (Exception e) {
            logger.error("Exception at ChatController.java: ", e);
        }

        return "Failed to return response for ChatController.java";
    }
}
