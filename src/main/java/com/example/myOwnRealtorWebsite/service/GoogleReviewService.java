package com.example.myOwnRealtorWebsite.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GoogleReviewService {

    @Value("${google.places.api-key}")
    private String apiKey;

    @Value("${google.places.place-id}")
    private String placeId;

    public List<Map<String, Object>> getGoogleReviews() {
        try {
            String url = "https://maps.googleapis.com/maps/api/place/details/json"
                    + "?place_id=" + placeId
                    + "&fields=reviews,rating,user_ratings_total"
                    + "&key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            Map response = restTemplate.getForObject(url, Map.class);

            if (response == null) return new ArrayList<>();

            Map result = (Map) response.get("result");
            if (result == null) return new ArrayList<>();

            List<Map<String, Object>> reviews =
                    (List<Map<String, Object>>) result.get("reviews");

            return reviews != null ? reviews : new ArrayList<>();

        } catch (Exception e) {
            System.err.println("Failed to fetch Google reviews: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Double getGoogleRating() {
        try {
            String url = "https://maps.googleapis.com/maps/api/place/details/json"
                    + "?place_id=" + placeId
                    + "&fields=rating,user_ratings_total"
                    + "&key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            Map response = restTemplate.getForObject(url, Map.class);
            Map result = (Map) response.get("result");
            if (result == null) return null;

            Object rating = result.get("rating");
            return rating != null ? ((Number) rating).doubleValue() : null;

        } catch (Exception e) {
            return null;
        }
    }
}