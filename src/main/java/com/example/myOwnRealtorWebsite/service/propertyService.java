package com.example.myOwnRealtorWebsite.service;

import com.example.myOwnRealtorWebsite.model.Property;
import com.example.myOwnRealtorWebsite.repository.propertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import java.math.BigDecimal;

@Service
public class propertyService {

    private final propertyRepository propRep;

    @Autowired
    public propertyService(propertyRepository propRep) {
        this.propRep = propRep;
    }

    public List<Property> searchProperties(String city, BigDecimal min,BigDecimal max) {
        //to ensure max price is greater than min price
        if(max.compareTo(min) < 0) {
            throw new IllegalArgumentException("Max price must be greater than min price");
        }
        return propRep.findByCityAndPriceBetween(city, min, max);

    }

    public Property saveProperty(Property property) {
        //you could automatically set the property status to pending,sold out
        return propRep.save(property);
    }
}
