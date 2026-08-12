package com.example.myOwnRealtorWebsite.controller;

import com.example.myOwnRealtorWebsite.model.Agent;
import com.example.myOwnRealtorWebsite.model.Inquiry;
import com.example.myOwnRealtorWebsite.model.Review;
import com.example.myOwnRealtorWebsite.model.User;
import com.example.myOwnRealtorWebsite.repository.reviewRepository;
import com.example.myOwnRealtorWebsite.service.inquiryService;
import com.example.myOwnRealtorWebsite.service.reviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class reviewController {
    private final reviewService service;
    private final inquiryService inqServ;

    public reviewController(reviewService service, inquiryService inqServ) {
        this.service = service;
        this.inqServ = inqServ;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Model model) {
        model.addAttribute("pendingReviews", service.getPendingReviews());
        model.addAttribute("pendingReviewCount", service.getPendingReviews().size());
        return "admin";
    }

    @GetMapping("/reviews")
    @PreAuthorize("hasRole('ADMIN')")
    public String reviewsPage(Model model) {
        model.addAttribute("pendingReviews", service.getPendingReviews());
        model.addAttribute("pendingReviewCount", service.getPendingReviews().size());
        return "admin";
    }

    @GetMapping("/messages")
    @PreAuthorize("hasRole('ADMIN')")
    public String messagesPage(Model model) {
        model.addAttribute("inquiries", inqServ.getAllInquiries());
        return "messages";
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public String approve(@PathVariable Long id) {
        service.approveReview(id);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String remove(@PathVariable Long id) {
        service.deleteReview(id);
        return "redirect:/admin";
    }

    @PostMapping("/messages/{id}/contacted")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void markContacted(@PathVariable Long id) {

        inqServ.updateStatus(id, Inquiry.inquiryStatus.CONTACTED);
    }

    @PostMapping("/messages/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void closeInquiry(@PathVariable Long id) {
        inqServ.updateStatus(id, Inquiry.inquiryStatus.CLOSED);
    }

//    @PostMapping("/reviews")
//    public String submitReview(@ModelAttribute Review review, Principal principal) {
//        User user = userServ.getByEmail(principal.getName());
//        review.setUser(user);
//        review.setAgentName("Sindhu Jakka"); // or pull from Agent bean if needed
//        service.saveReview(review);
//        return "redirect:/reviews";
//    }

}
