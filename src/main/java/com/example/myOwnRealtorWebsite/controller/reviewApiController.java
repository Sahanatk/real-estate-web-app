package com.example.myOwnRealtorWebsite.controller;

import com.example.myOwnRealtorWebsite.model.Review;
import com.example.myOwnRealtorWebsite.service.reviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class reviewApiController {
    private final reviewService reviewService;

    @Autowired
    public reviewApiController(reviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/submit")
    public ResponseEntity<Review> submit(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.saveReview(review));
    }

    @GetMapping("/featured")
    public List<Review> getFeatured() {
        return reviewService.getTopReviewsForHomePage();
    }

}
