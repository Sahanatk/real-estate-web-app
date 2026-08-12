package com.example.myOwnRealtorWebsite.repository;

import com.example.myOwnRealtorWebsite.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface propertyRepository extends JpaRepository<Property,Long> {
    //find properties by city and price range
    List<Property> findByCityAndPriceBetween(String city, BigDecimal minPrice,BigDecimal maxPrice);

    // find property by status like for-sale
    List<Property> findByStatus(String status);
}
