package com.example.myOwnRealtorWebsite.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Entity
@Table(name = "Inquiry")
@Data
public class Inquiry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = true)
    private User user;

    @Column(name = "visitor_name")
    private String visitorName;

    @Column(name = "visitor_email")
    private String visitorEmail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "listing_id",nullable = true)
    private Listing property;

    private String message;

    public String getVisitorName() { return visitorName; }
    public void setVisitorName(String visitorName) { this.visitorName = visitorName; }
    public String getVisitorEmail() { return visitorEmail; }
    public void setVisitorEmail(String visitorEmail) { this.visitorEmail = visitorEmail; }

    public Listing getProperty() {
        return property;
    }

    public void setProperty(Listing property) {
        this.property = property;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public inquiryStatus getStatus() {
        return status;
    }

    public void setStatus(inquiryStatus status) {
        this.status = status;
    }


    //private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    private inquiryStatus status;

    public enum inquiryStatus {
        NEW,CONTACTED,CLOSED
    }

}
