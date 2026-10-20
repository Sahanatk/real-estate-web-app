package com.example.myOwnRealtorWebsite.dto;

public record ListingDescriptionRequest(
        String propertyType,
        String city,
        String state,
        Integer bedrooms,
        Double bathrooms,
        Double squareFootage,
        Double lotSize,
        Integer yearBuilt,
        String garageDetails,
        Double listingPrice,
        Double hoaFees,
        String highlights   // optional free text, e.g. "ocean view, remodeled kitchen"
) {}
