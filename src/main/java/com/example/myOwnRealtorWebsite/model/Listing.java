package com.example.myOwnRealtorWebsite.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Listing")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listing_id")
    private Long listingId;

    // --- Link to Agent ---
    @Transient // tells hibernate not to link to DB
    private String agentName;

    @Column(columnDefinition =  "TEXT")
    private String description;

    // --- Address Details ---
    @NotBlank(message = "Street address is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "state is required")
    private String state;

    private String zipCode;

    // --- Property Specs ---
    @NotNull(message = "Property type must be selected")
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType; // Residential, Commercial, Land

    @PositiveOrZero(message = "Square footage cannot be negative")
    private Double squareFootage;
    private Double lotSize;
    private Integer yearBuilt;

    @Min(value = 1, message = "At least one bedroom is required")
    private Integer bedrooms;
    private Double bathrooms; // Supports 2.5 for half-baths
    private String garageDetails;

    // --- Pricing & Finance ---
    @Min(value = 0, message = "Price must be a positive number")
    @NotNull(message = "Listing price is required")
    @NumberFormat(pattern = "#,##0.##")
    private Double listingPrice;

    private String taxInformation;

    @NumberFormat(pattern = "#,##0.##")
    private Double hoaFees;

    // --- Features & Media ---
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> propertyFeatures = new ArrayList<>(); // pool, view, HVAC

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "listing_photos",           // ← exact table name in DB
            joinColumns = @JoinColumn(name = "listing_id")  // ← exact FK column name
    )
    @Column(name = "photo_url",length = 1000)
    private List<String> photos; // URLs to media files

    private String listingUrl;

    // --- Status & Tracking ---

    @Enumerated(EnumType.STRING)
    private ListingStatus status; // Active, Pending, Sold, Expired

    private LocalDate listingDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime lastModified; // Listing Modification DateTime


    // --- Supporting Enums ---
    public enum PropertyType {
        RESIDENTIAL, COMMERCIAL, LAND
    }

    public enum ListingStatus {
        ACTIVE,PENDING,SOLD,EXPIRED
    }
    public String getFirstPhoto() {
        if (photos == null || photos.isEmpty()) return null;
        return photos.get(0);
    }
}
