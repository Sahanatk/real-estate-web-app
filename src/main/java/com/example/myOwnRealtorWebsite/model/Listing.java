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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public Double getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(Double squareFootage) {
        this.squareFootage = squareFootage;
    }

    public Double getLotSize() {
        return lotSize;
    }

    public void setLotSize(Double lotSize) {
        this.lotSize = lotSize;
    }

    public Integer getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(Integer yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Double getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Double bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getGarageDetails() {
        return garageDetails;
    }

    public void setGarageDetails(String garageDetails) {
        this.garageDetails = garageDetails;
    }

    public Double getListingPrice() {
        return listingPrice;
    }

    public void setListingPrice(Double listingPrice) {
        this.listingPrice = listingPrice;
    }

    public String getTaxInformation() {
        return taxInformation;
    }

    public void setTaxInformation(String taxInformation) {
        this.taxInformation = taxInformation;
    }

    public Double getHoaFees() {
        return hoaFees;
    }

    public void setHoaFees(Double hoaFees) {
        this.hoaFees = hoaFees;
    }

    public List<String> getPropertyFeatures() {
        return propertyFeatures;
    }

    public void setPropertyFeatures(List<String> propertyFeatures) {
        this.propertyFeatures = propertyFeatures;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getListingUrl() {
        return listingUrl;
    }

    public void setListingUrl(String listingUrl) {
        this.listingUrl = listingUrl;
    }

    public ListingStatus getStatus() {
        return status;
    }

    public void setStatus(ListingStatus status) {
        this.status = status;
    }

    public LocalDate getListingDate() {
        return listingDate;
    }

    public void setListingDate(LocalDate listingDate) {
        this.listingDate = listingDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

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
    private Double listingPrice;

    private String taxInformation;
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
