package com.example.myOwnRealtorWebsite.service;

import com.example.myOwnRealtorWebsite.model.Agent;
import com.example.myOwnRealtorWebsite.model.Listing;
import com.example.myOwnRealtorWebsite.model.Property;
import com.example.myOwnRealtorWebsite.repository.inquiryRepository;
import com.example.myOwnRealtorWebsite.repository.listingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class listingService {
    private inquiryRepository inqRep;
    private final listingRepository repository;
    private final Agent agentProfile; // Loaded from properties


    @Autowired
    public listingService(listingRepository repository, Agent agentProfile,inquiryRepository inqRep) {
        this.repository = repository;
        this.agentProfile = agentProfile;
        this.inqRep = inqRep;
    }

    public Listing createListing(Listing listing) {
        // Automatically stamp the listing with the agent's name
        listing.setAgentName(agentProfile.getName());
        return repository.save(listing);
    }

    public List<Listing> getAgentListings(String status) {
        // Query database for all listings matching the name in Listing database
        return repository.findByStatus(status);
    }

    public Listing getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
    }

    public void saveNewListing(Listing listing, String features, List<String> photoUrls) {
        // Business Rule: Stamp with the current agent's name
        listing.setAgentName(agentProfile.getName());

        // Data Integrity: Clean up the lists
        // 1. Process Features: Only add if text isn't blank
        if (features != null && ! features.isBlank()) {
            //listing.setPropertyFeatures(Arrays.asList(features.split("\\s*,\\s*")));
            listing.setPropertyFeatures(Arrays.asList(features.split("\\s*,\\s*(?=[^0-9])")));
        }
        // 2. Process Photos: Only add if text isn't blank
        if (photoUrls != null && !photoUrls.isEmpty()) {
            listing.setPhotos(photoUrls);
        }

        // Business Rule: Set default status if missing
        if (listing.getStatus() == null) {
            listing.setStatus(Listing.ListingStatus.ACTIVE);
        }
        repository.save(listing);
    }
    public List<Listing> getAllListings() {
        return repository.findAll();
    }

    public void save(Listing listing) {
        repository.save(listing);
    }

    @Transactional
    public void delete(Long id) {
        //inquiryRepository.deleteByPropertyListingId(id);
        repository.deleteById(id);
    }

    @Transactional
    public void updatePhotos(Long id, List<String> photoUrls) {
        Listing listing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
        listing.getPhotos().clear();        // delete old photos first
        listing.getPhotos().addAll(photoUrls); // add new ones
        repository.save(listing);
    }

}
