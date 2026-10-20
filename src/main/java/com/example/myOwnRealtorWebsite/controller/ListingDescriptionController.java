package com.example.myOwnRealtorWebsite.controller;

import com.example.myOwnRealtorWebsite.dto.ListingDescriptionRequest;
import com.example.myOwnRealtorWebsite.service.DescriptionGenerationException;
import com.example.myOwnRealtorWebsite.service.ListingDescriptionService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/listings")
public class ListingDescriptionController {

    private final ListingDescriptionService service;

    public ListingDescriptionController(ListingDescriptionService service) {
        this.service = service;
    }

    @PostMapping("/generate-description")
    public ResponseEntity<Map<String, String>> generate(@RequestBody ListingDescriptionRequest request) {
        try {
            return ResponseEntity.ok(Map.of("description", service.generate(request)));
        } catch (DescriptionGenerationException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}