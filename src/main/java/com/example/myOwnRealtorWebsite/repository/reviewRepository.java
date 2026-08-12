package com.example.myOwnRealtorWebsite.repository;

import com.example.myOwnRealtorWebsite.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface reviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByAgentName(String agentName);

    //finds only approved reviews for the homepage, newest first
    List<Review> findTop3ByStatusOrderByCreatedAtDesc(String status);

    //delete method
    void deleteById(Long id);

    List<Review> findByStatus(String status);
}
