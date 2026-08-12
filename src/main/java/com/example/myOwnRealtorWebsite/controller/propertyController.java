package com.example.myOwnRealtorWebsite.controller;


import com.example.myOwnRealtorWebsite.Views;
import com.example.myOwnRealtorWebsite.model.Agent;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/properties")
public class propertyController {
    private final Agent agent;

    public propertyController(Agent agent) {
        this.agent = agent;
    }

    @GetMapping("/agent-info")
    @JsonView(Views.Public.class)       //shows non-sensitive data
    public Agent getAgentData() {
        return agent;
    }
}
