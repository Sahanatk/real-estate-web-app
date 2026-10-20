package com.example.myOwnRealtorWebsite.service;

import com.example.myOwnRealtorWebsite.dto.ListingDescriptionRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class ListingDescriptionService {

    private static final String SYSTEM_PROMPT = """
            You are a professional real estate copywriter.
            Write an appealing listing description of 120 to 180 words in a warm, professional tone.
            Use ONLY the facts provided. Never invent features, amenities, or numbers.
            Do not mention the price.
            Follow U.S. Fair Housing rules: describe the property, not who should live there.
            Avoid phrases like "perfect for families", "ideal for singles", or references to
            religion, race, national origin, disability, or familial status.
            Return only the description text, with no title or notes.
            """;

    private final RestClient restClient;
    private final String model;
    private final boolean mock;

    public ListingDescriptionService(RestClient anthropicRestClient,
                                     @Value("${anthropic.model}") String model,
                                     @Value("${anthropic.mock:false}") boolean mock) {
        this.restClient = anthropicRestClient;
        this.model = model;
        this.mock = mock;
    }

    public String generate(ListingDescriptionRequest req) {
        if (mock) {
            return "[MOCK] Charming " + req.bedrooms() + "-bedroom home in " + req.city()
                    + ". This is a sample description for local testing.";
        }
        Map<String, Object> body = Map.of(
                "model", model,
                "max_tokens", 400,
                "system", SYSTEM_PROMPT,
                "messages", List.of(Map.of("role", "user", "content", buildPrompt(req)))
        );

        try {
            AnthropicResponse response = restClient.post()
                    .uri("/v1/messages")
                    .body(body)
                    .retrieve()
                    .body(AnthropicResponse.class);

            if (response == null || response.content() == null || response.content().isEmpty()) {
                throw new DescriptionGenerationException("The AI returned an empty response.");
            }
            return response.content().get(0).text().trim();

        } catch (RestClientException e) {
            throw new DescriptionGenerationException("Couldn't generate a description right now. Please try again.", e);
        }
    }

    // Package-private so it can be unit tested
    String buildPrompt(ListingDescriptionRequest r) {
        StringBuilder sb = new StringBuilder("Write a listing description for this property:\n");
        append(sb, "Property type", r.propertyType());
        append(sb, "Location", joinLocation(r.city(), r.state()));
        append(sb, "Bedrooms", r.bedrooms());
        append(sb, "Bathrooms", r.bathrooms());
        append(sb, "Square footage", r.squareFootage());
        append(sb, "Lot size", r.lotSize());
        append(sb, "Year built", r.yearBuilt());
        append(sb, "Garage", r.garageDetails());
        append(sb, "HOA fees per month", r.hoaFees());
        append(sb, "Highlights", r.highlights());
        return sb.toString();
    }

    private void append(StringBuilder sb, String label, Object value) {
        if (value != null && !value.toString().isBlank()) {
            sb.append("- ").append(label).append(": ").append(value).append('\n');
        }
    }

    private String joinLocation(String city, String state) {
        if (city == null || city.isBlank()) return state;
        if (state == null || state.isBlank()) return city;
        return city + ", " + state;
    }

    // Maps just the part of the API response we need
    record AnthropicResponse(List<ContentBlock> content) {}
    record ContentBlock(String type, String text) {}
}
