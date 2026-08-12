package com.example.myOwnRealtorWebsite.model;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private Long propertyId;

    @NotBlank(message = "Title is required")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @DecimalMin(value = "0.0",inclusive = false)
    private BigDecimal price;

    @NotBlank
    private String address;
    private String city;

    @Enumerated(EnumType.STRING)
    private PropertyType type; // e.g., APARTMENT, VILLA, CONDO

    @Enumerated(EnumType.STRING)
    private ListingStatus status; // e.g., FOR_SALE, FOR_RENT, SOLD

    private int bedrooms;
    private int bathrooms;

    @Column(name = "square_footage", nullable = false)
    private double squareFootage;

    @ElementCollection
    private List<String> imageUrls;


    private String agentName; // The realtor who listed it

    public enum PropertyType {
        APARTMENT, HOUSE, VILLA, CONDO, LAND
    }

    public enum ListingStatus {
        FOR_SALE, FOR_RENT, SOLD, PENDING
    }
}
