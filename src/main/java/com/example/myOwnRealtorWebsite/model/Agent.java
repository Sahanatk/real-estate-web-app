package com.example.myOwnRealtorWebsite.model;

import com.example.myOwnRealtorWebsite.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


//@PropertySource(value = "classpath:agentDetails.properties", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "agent")
@Data //generates getters and setters

public class Agent {

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonView(Views.Internal.class) // hidden from public
    private String licenseNumber;

    @JsonView(Views.Public.class) //visible to everyone
    private String profilePictureUrl;

    @JsonView(Views.Public.class)
    private String homePageUrl;

    @JsonView(Views.Public.class)
    private String biography;

    @JsonView(Views.Public.class)
    private String speciality;// residential, luxury condos

    @JsonView(Views.Public.class)
    private int yearsOfExperience;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonView(Views.Internal.class)
    private double totalSalesVolume;

}
