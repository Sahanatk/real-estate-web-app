package com.example.myOwnRealtorWebsite.controller;

import com.example.myOwnRealtorWebsite.Views;
import com.example.myOwnRealtorWebsite.model.Agent;
import com.example.myOwnRealtorWebsite.service.GoogleReviewService;
import com.example.myOwnRealtorWebsite.service.reviewService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
public class globalControllerAdvice {

    private final Agent agent;
    private final GoogleReviewService googRevServ;

    public globalControllerAdvice(Agent agent, reviewService reviewServ, GoogleReviewService googRevServ) {
        this.agent = agent;
        this.googRevServ = googRevServ;
    }

    @ModelAttribute("agent")
    public Agent getAgent() {
        return agent;
    }
    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("agent", agent);
        model.addAttribute("googleReviews", googRevServ.getGoogleReviews());
        model.addAttribute("googleRating", googRevServ.getGoogleRating());
    }

}
