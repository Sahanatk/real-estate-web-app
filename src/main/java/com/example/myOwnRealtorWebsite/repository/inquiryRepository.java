package com.example.myOwnRealtorWebsite.repository;

import com.example.myOwnRealtorWebsite.model.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface inquiryRepository extends JpaRepository<Inquiry,Long> {
    //find all inquiries with a specific status
    List<Inquiry> findByStatus(Inquiry.inquiryStatus status);

    //fina all inquires for a specific property with a specific status
    List<Inquiry> findByIdAndStatus(Long listingId, Inquiry.inquiryStatus status);

    //passing a list of statuses to filter multiple at once
    List<Inquiry> findByStatusIn(List<Inquiry.inquiryStatus> statuses);

    List<Inquiry> findAllByOrderByIdDesc();

    @Transactional
    @Modifying
     void deleteByPropertyListingId(Long listingId);
}
