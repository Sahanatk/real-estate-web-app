package com.example.myOwnRealtorWebsite.controller;

import com.example.myOwnRealtorWebsite.Views;
import com.example.myOwnRealtorWebsite.model.Agent;
import com.example.myOwnRealtorWebsite.model.Listing;
import com.example.myOwnRealtorWebsite.model.Review;
import com.example.myOwnRealtorWebsite.repository.reviewRepository;
import com.example.myOwnRealtorWebsite.service.listingService;
import com.example.myOwnRealtorWebsite.service.reviewService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/api/agent")
public class agentController {
    private final reviewRepository reviewRepo;
    private final Agent agent;
    private final reviewService reviewServ;
    private final listingService listServ;

    public agentController(reviewRepository reviewRepo, Agent agent, reviewService reviewServ, listingService listServ) {
        this.reviewRepo = reviewRepo;
        this.agent = agent;
        this.reviewServ = reviewServ;
        this.listServ = listServ;
    }


    @GetMapping("/debug")
    @ResponseBody
    public String debug(Model model) {
        List<Review> reviews = reviewServ.getTopReviewsForHomePage();
        return "Agent name: " + agent.getName() +
                " | Photo: " + agent.getProfilePictureUrl() +
                " | Bio length: " + (agent.getBiography() != null ? agent.getBiography().length() : "NULL") +
                " | Reviews count: " + reviews.size() +
                " | Average: " + reviewServ.getAverageRating();
    }

    @GetMapping("/agent-details")     //shows all info to the public except the hidden details
    @ResponseBody
    @JsonView(Views.Public.class)
    public Agent getAgentDetails() {
        return agent;
    }

    @GetMapping("/fullAgentProfile") //hides license number and total sales volume
    @ResponseBody
    @JsonView(Views.Internal.class)
    public Agent getFullAgentDetails() {
        return agent;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
       // model.addAttribute("agent", agent.getName());
        List<Review> reviews = reviewServ.getTopReviewsForHomePage();
        double avgRating = reviewServ.getAverageRating();
        model.addAttribute("reviews", reviewServ.getTopReviewsForHomePage());
        model.addAttribute("averageRating", reviewServ.getAverageRating());
        model.addAttribute("averageRating", avgRating);
        return "profile"; // Refers to src/main/resources/templates/profile.html
    }

    public double getAverageRating() {
        List<Review> approved = reviewRepo.findByStatus("approved");
        if (approved == null || approved.isEmpty()) return 0.0;
        return approved.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

//    @GetMapping("/addListing")
//    public String addListing(Model model) {
//        model.addAttribute("listing",new Listing());
//        return "addListing";
//    }
}
