package com.example.myOwnRealtorWebsite.service;

import com.example.myOwnRealtorWebsite.model.Inquiry;
import com.example.myOwnRealtorWebsite.model.Listing;
import com.example.myOwnRealtorWebsite.model.Property;
import com.example.myOwnRealtorWebsite.model.User;
import com.example.myOwnRealtorWebsite.repository.inquiryRepository;
import com.example.myOwnRealtorWebsite.repository.listingRepository;
import com.example.myOwnRealtorWebsite.repository.propertyRepository;
import com.example.myOwnRealtorWebsite.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class inquiryService {

    private final inquiryRepository inqRep;
    private final listingRepository listRepo;
    private final userRepository userRep;

    Inquiry inq = new Inquiry();

    @Autowired
    public inquiryService(userRepository userRep, inquiryRepository inqRep, listingRepository listRep) {
        this.userRep = userRep;
        this.inqRep = inqRep;
        this.listRepo = listRep;
    }

    public Inquiry createInquiry(Long userId, Long listingId, String message) {
        //to fetch the user by its id
        User user = userRep.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        //to fetch the property by its id
        Listing property = listRepo.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        //setting the values on the object
        inq.setUser(user);
        inq.setProperty(property);
        inq.setMessage(message);
        inq.setStatus(Inquiry.inquiryStatus.NEW);
        //use the repository to save the object to the database
        return inqRep.save(inq);
    }

    public Inquiry updateInquiryStatus(Long inquiryId, Inquiry.inquiryStatus newStatus) {
        Inquiry inquiry = inqRep.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        inquiry.setStatus(newStatus);
        return inqRep.save(inquiry);
    }

    public List<Inquiry> getInquiriesByStatus(Inquiry.inquiryStatus status) {
        return inqRep.findByStatus(status);
    }

    public List<Inquiry> getActiveInquiries() {
        return inqRep.findByStatusIn(List.of(Inquiry.inquiryStatus.NEW, Inquiry.inquiryStatus.CONTACTED));
    }

    public List<Inquiry> getAllInquiries() {
        return inqRep.findAllByOrderByIdDesc();
    }

    public Inquiry save(Inquiry inquiry) {
        return inqRep.save(inquiry);
    }
    public void updateStatus(Long id, Inquiry.inquiryStatus status) {
        Inquiry inquiry = inqRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        inquiry.setStatus(status);
        inqRep.save(inquiry);
    }
}
