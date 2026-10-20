package com.example.myOwnRealtorWebsite.service;

import com.example.myOwnRealtorWebsite.model.Agent;
import com.example.myOwnRealtorWebsite.model.Listing;
import com.example.myOwnRealtorWebsite.repository.inquiryRepository;
import com.example.myOwnRealtorWebsite.repository.listingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @Mock listingRepository repository;   // fake database
    @Mock Agent agentProfile;              // fake agent
    @Mock inquiryRepository inqRep;        // fake (not used, but the constructor needs it)

    @InjectMocks listingService service;   // REAL service, with the fakes injected

    @BeforeEach
    void setUp() {
        // lenient = "it's OK if some tests don't use this"
        lenient().when(agentProfile.getName()).thenReturn("Sindhu Jakka");
    }

    // ---------- saveNewListing: features ----------

    @Test
    void saveNewListing_splitsFeatures_keepsThousandsTogether() {
        Listing listing = new Listing();

        service.saveNewListing(listing, "Pool, 3-car garage, 2,000 sqft deck, Garden", List.of());

        assertEquals(List.of("Pool", "3-car garage", "2,000 sqft deck", "Garden"),
                listing.getPropertyFeatures());
    }

    @Test
    void saveNewListing_ignoresBlankEntries() {
        Listing listing = new Listing();

        service.saveNewListing(listing, "Pool,, Garden, ", List.of());

        assertEquals(List.of("Pool", "Garden"), listing.getPropertyFeatures());
    }

    @Test
    void saveNewListing_blankFeatures_savesNoTags() {
        Listing listing = new Listing();

        service.saveNewListing(listing, "   ", List.of());

        assertTrue(listing.getPropertyFeatures() == null || listing.getPropertyFeatures().isEmpty());
    }

    // ---------- saveNewListing: business rules ----------

    @Test
    void saveNewListing_stampsAgentName() {
        Listing listing = new Listing();

        service.saveNewListing(listing, "", List.of());

        assertEquals("Sindhu Jakka", listing.getAgentName());
    }

    @Test
    void saveNewListing_defaultsStatusToActive_whenMissing() {
        Listing listing = new Listing();   // status is null

        service.saveNewListing(listing, "", List.of());

        assertEquals(Listing.ListingStatus.ACTIVE, listing.getStatus());
    }

    @Test
    void saveNewListing_setsPhotos_whenProvided() {
        Listing listing = new Listing();
        List<String> urls = List.of("https://img/1.jpg", "https://img/2.jpg");

        service.saveNewListing(listing, "", urls);

        assertEquals(urls, listing.getPhotos());
    }

    @Test
    void saveNewListing_savesToRepository() {
        Listing listing = new Listing();

        service.saveNewListing(listing, "Pool", List.of());

        ArgumentCaptor<Listing> saved = ArgumentCaptor.forClass(Listing.class);
        verify(repository).save(saved.capture());     // save() was called exactly once
        assertSame(listing, saved.getValue());        // with OUR listing
    }

    // ---------- getById ----------

    @Test
    void getById_returnsListing_whenFound() {
        Listing listing = new Listing();
        when(repository.findById(1L)).thenReturn(Optional.of(listing));

        assertSame(listing, service.getById(1L));
    }

    @Test
    void getById_throws_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getById(99L));
        assertEquals("Listing not found", ex.getMessage());
    }

    // ---------- updatePhotos ----------

    @Test
    void updatePhotos_replacesOldPhotosWithNew() {
        Listing listing = new Listing();
        listing.setPhotos(new ArrayList<>(List.of("old1.jpg", "old2.jpg")));
        when(repository.findById(5L)).thenReturn(Optional.of(listing));

        service.updatePhotos(5L, List.of("new1.jpg"));

        assertEquals(List.of("new1.jpg"), listing.getPhotos());
        verify(repository).save(listing);
    }

    // ---------- delete ----------

    @Test
    void delete_callsRepository() {
        service.delete(7L);

        verify(repository).deleteById(7L);
    }
}
