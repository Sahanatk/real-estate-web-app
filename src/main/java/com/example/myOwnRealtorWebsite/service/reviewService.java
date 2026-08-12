package com.example.myOwnRealtorWebsite.service;

import com.example.myOwnRealtorWebsite.model.Agent;
import com.example.myOwnRealtorWebsite.model.Review;
import com.example.myOwnRealtorWebsite.repository.reviewRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class reviewService {
    private final reviewRepository reviewRepo;
    private final Agent agent;

    @Autowired
    public reviewService(reviewRepository reviewRepo, Agent agent) {
        this.reviewRepo = reviewRepo;
        this.agent = agent;
    }

//saves a review and automatically stamps it with agent name
    public Review saveReview(Review review) {
        review.setAgentName(agent.getName());

        //basic validation to ensure rating is between  1 to 5
        if(review.getRating() < 1 || review.getRating() > 5 ) {
            throw new IllegalArgumentException("Rating must be between 1 to 5 stars");
        }
        return reviewRepo.save(review);
    }

    public List<Review> findAllReviews() {
        return reviewRepo.findAll();
    }

    public Double getAverageRating() {
        List<Review> reviews = reviewRepo.findAll();

        //use stream to calculate average
        return reviews.stream()
            .mapToInt(Review::getRating)
            .average()
            .orElse(0.0);       //return 0.0 if no review exists
    }

    //Approving the review
    @PreAuthorize("hasRole('ADMIN')")
    public Review approveReview(Long id) {
        Review review = reviewRepo.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setStatus("approved");
        return reviewRepo.save(review);
    }

    //deleting
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteReview(Long id) {
        reviewRepo.deleteById(id);
    }

    //showing top 3 reviews
    public List<Review> getTopReviewsForHomePage() {
        return reviewRepo.findTop3ByStatusOrderByCreatedAtDesc("approved");
    }


    public List<Review> getAllReviews() {
        return reviewRepo.findAll();
    }

    public List<Review> getPendingReviews() {
        return reviewRepo.findByStatus("pending");
    }


    public List<Review> getApprovedReviews() {
        return reviewRepo.findByStatus("approved");
    }
}
