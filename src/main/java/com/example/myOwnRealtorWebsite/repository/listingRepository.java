package com.example.myOwnRealtorWebsite.repository;

import com.example.myOwnRealtorWebsite.model.Listing;
import com.example.myOwnRealtorWebsite.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface listingRepository extends JpaRepository<Listing,Long> {
    List<Listing> findByStatus(String status);

    Listing getById(Long id)
;    //List<Listing> findListingId(Long listingId);
}
