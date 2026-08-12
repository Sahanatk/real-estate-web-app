package com.example.myOwnRealtorWebsite.controller;

import com.example.myOwnRealtorWebsite.Views;
import com.example.myOwnRealtorWebsite.model.Agent;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class userController {
    private final Agent agent;

    public userController(Agent agent) {
        this.agent = agent;
    }

    @GetMapping("/full-agent-info")
    @JsonView(Views.Internal.class)
    public Agent getFullAgentData() {
        return agent;
    }
}
