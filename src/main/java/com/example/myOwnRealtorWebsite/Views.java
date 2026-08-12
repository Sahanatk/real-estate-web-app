package com.example.myOwnRealtorWebsite;

public class Views {
    //basic info for the public website
    public interface Public {}

//sensitive info for authorized users
    public interface Internal extends Public {}
}
