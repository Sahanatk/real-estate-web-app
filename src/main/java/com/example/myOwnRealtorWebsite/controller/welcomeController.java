package com.example.myOwnRealtorWebsite.controller;

import com.example.myOwnRealtorWebsite.model.*;
import com.example.myOwnRealtorWebsite.service.inquiryService;
import com.example.myOwnRealtorWebsite.service.listingService;
import com.example.myOwnRealtorWebsite.service.reviewService;
import com.example.myOwnRealtorWebsite.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;


@Controller
public class welcomeController {

        private final reviewService reviewServ;
        private final listingService listServ;
        private final userService userServ;
        private final inquiryService inquServ;

        @Autowired
        public welcomeController(reviewService reviewServ, listingService listServ, userService userServ, inquiryService inquServ) {
            this.reviewServ = reviewServ;
            this.listServ = listServ;
            this.userServ = userServ;
            this.inquServ = inquServ;
        }

    // Automatically adds the agent info to every page mapped in this controller
        @GetMapping("/")
        public String index(Model model) {
            // Fetch only the top 3 approved reviews for the home page
            model.addAttribute("reviews", reviewServ.getTopReviewsForHomePage());
            return "index";
        }

        @GetMapping("/services")
        public String services() {
            return "services";
        }

//        @GetMapping("/contact")
//        public String contact() {
//            return "contact";
//        }

        @GetMapping("/reviews")
        public String allReviews(Model model) {
            model.addAttribute("reviews", reviewServ.getApprovedReviews());
            model.addAttribute("averageRating",reviewServ.getAverageRating());
            model.addAttribute("review", new Review()); // For the form binding
            return "review";
        }

    @GetMapping("/review-form")
    public String reviewForm(Model model) {
        model.addAttribute("review", new Review());
        return "review-form"; // template name of that separate review submission page
    }

    @PostMapping("/reviews")
    public String submitReview(@ModelAttribute Review review, Principal principal,Model model) {
        System.out.println("=== DEBUG: principal.getName() = [" + principal.getName() + "] ===");
        User user = userServ.getByEmail(principal.getName());

        review.setUser(user);
        review.setAgentName("Sindhu Jakka"); // or pull from Agent bean if needed
        reviewServ.saveReview(review);
        model.addAttribute("success", true);
        model.addAttribute("review", new Review()); // reset the form
        return "redirect:/reviews";
    }

    @GetMapping("/contact")
    public String genericContact () {
            return "contact";
    }

    @GetMapping("/contact/{listingId}")
    public String contact(@PathVariable Long listingId,Model model) {
        Listing listing = listServ.getById(listingId);
        model.addAttribute("listing", listing);
        return "contact";
    }

    @PostMapping("/contact")
    public String submitContact(@RequestParam String message,
                                @RequestParam(required = false) Long listingId,
                                @RequestParam(required = false) String visitorName,
                                @RequestParam(required = false) String visitorEmail,
                                Principal principal,
                                Model model) {
        System.out.println("=== visitorName: " + visitorName + " | visitorEmail: " + visitorEmail + " ===");
        Inquiry inquiry = new Inquiry();
        inquiry.setMessage(message);
        inquiry.setStatus(Inquiry.inquiryStatus.NEW);

        // Only set user if logged in
        if (principal != null && !principal.getName().equals("agent_admin")) {
            User user = userServ.getByEmail(principal.getName());
            inquiry.setUser(user);
        } else {
            // Store visitor info in message
            inquiry.setVisitorName(visitorName);
            inquiry.setVisitorEmail(visitorEmail);
        }

        if (listingId != null) {
            Listing listing = listServ.getById(listingId);
            inquiry.setProperty(listing);
            model.addAttribute("listing", listing);
        }

        inquServ.save(inquiry);
        model.addAttribute("success", true);
        return "contact";
    }


    @GetMapping("/listings")
    public String allListings(Model model) {
        List<Listing> listings = listServ.getAllListings();
       // System.out.println("=== LISTINGS COUNT: " + listings.size() + " ===");
//        listings.forEach(l -> System.out.println(
//                "ID: " + l.getListingId() +
//                        " | photos size: " + (l.getPhotos() != null ? l.getPhotos().size() : "NULL") +
//                        " | firstPhoto: " + l.getFirstPhoto()
//        ));
        model.addAttribute("listings", listings);
        return "listings";
    }

    @GetMapping("/listings/{id}")
    public String listingDetails(@PathVariable Long id, Model model) {
        Listing listing = listServ.getById(id);
        model.addAttribute("listing", listing);
        return "listingDetails";
    }


}
