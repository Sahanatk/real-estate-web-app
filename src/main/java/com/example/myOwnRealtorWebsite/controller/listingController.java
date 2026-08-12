package com.example.myOwnRealtorWebsite.controller;

import com.example.myOwnRealtorWebsite.model.Listing;

import com.example.myOwnRealtorWebsite.service.fileUploadService;
import com.example.myOwnRealtorWebsite.service.listingService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Controller
@RequestMapping("/admin")
public class listingController {

    private final listingService listServ;
    private final fileUploadService fileUploadService;

    public listingController(listingService listServ, fileUploadService fileUploadService) {
        this.listServ = listServ;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/newListing")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddListingForm(Model model) {
        model.addAttribute("listing", new Listing());
        // Pass Enum values to the frontend for the dropdowns
        model.addAttribute("propertyTypes", Listing.PropertyType.values());
        model.addAttribute("statuses", Listing.ListingStatus.values());
        return "addListing";
    }

//    @PostMapping("/save")
//    public String saveListing(@Valid  @ModelAttribute("listing") Listing listing, BindingResult bindingResult,
//                              @RequestParam("propertyFeatures") String features,
//                              @RequestParam("photos") String photos) {
//
//        listServ.saveNewListing(listing, features, photos);
//
//        return "redirect:/admin";
//    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveListing(@Valid @ModelAttribute("listing") Listing listing,
                              BindingResult bindingResult,
                              @RequestParam("propertyFeatures") String features,
                              @RequestParam("photoFiles") MultipartFile[] photoFiles,
                              RedirectAttributes redirectAttributes,Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            return "addListing";
        }

        // Upload photos and get URLs
        try {
            List<String> photoUrls = fileUploadService.uploadPhotos(photoFiles);

            // Save listing with photo URLs
            listServ.saveNewListing(listing, features, photoUrls);
            // ← success message
            redirectAttributes.addFlashAttribute("success",
                    "Listing published successfully! " + photoUrls.size() + " photo(s) uploaded.");

            return "redirect:/listings";  // ← redirect to listings page
        } catch (Exception e) {
            model.addAttribute("error", "Failed to publish listing: " + e.getMessage());
            model.addAttribute("propertyTypes", Listing.PropertyType.values());
            model.addAttribute("statuses", Listing.ListingStatus.values());
            return "addListing";
        }
    }

    @GetMapping("/listings/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editListing(@PathVariable Long id, Model model) {
        model.addAttribute("listing", listServ.getById(id));
        model.addAttribute("propertyTypes", Listing.PropertyType.values());
        model.addAttribute("statuses", Listing.ListingStatus.values());
        model.addAttribute("editMode", true);
        return "addListing";
    }

    @PostMapping("/listings/{id}/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateListing(@PathVariable Long id,
                                @ModelAttribute Listing listing,
                                @RequestParam("propertyFeatures") String features,
                                @RequestParam("photoFiles") MultipartFile[] photoFiles,
                                RedirectAttributes redirectAttributes) throws IOException {
        listing.setListingId(id);
        List<String> photoUrls = fileUploadService.uploadPhotos(photoFiles);
        if (photoUrls.isEmpty()) {
            // Keep existing photos if no new ones uploaded
            List<String> existingPhotos = listServ.getById(id).getPhotos();
            listing.setPhotos(existingPhotos);
        }
        listServ.saveNewListing(listing, features, photoUrls.isEmpty() ? null : photoUrls);
        redirectAttributes.addFlashAttribute("success", "Listing updated successfully!");
        return "redirect:/listings/" + id;
    }

    @PostMapping("/listings/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteListing(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            // Delete photos first
            List<String> photos = listServ.getById(id).getPhotos();
            // If using Cloudinary, optionally delete from there too
            listServ.delete(id);
            redirectAttributes.addFlashAttribute("success", "Listing deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not delete listing: " + e.getMessage());
        }
        return "redirect:/listings";
    }

    @PostMapping("/listings/{id}/update-photos")
    @PreAuthorize("hasRole('ADMIN')")
    public String updatePhotos(@PathVariable Long id,
                               @RequestParam("photoFiles") MultipartFile[] photoFiles,
                               RedirectAttributes redirectAttributes) throws IOException {
      //  System.out.println("=== UPDATE PHOTOS CALLED for listing: " + id + " ===");
        try {
            List<String> photoUrls = fileUploadService.uploadPhotos(photoFiles);
           // System.out.println("=== UPLOADED " + photoUrls.size() + " photos ===");
            if (!photoUrls.isEmpty()) {
                listServ.updatePhotos(id, photoUrls); // ← use new method
                redirectAttributes.addFlashAttribute("success",
                        photoUrls.size() + " photo(s) uploaded successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "No photos were uploaded.");
            }
        } catch (Exception e) {
           // System.out.println("=== UPLOAD ERROR: " + e.getMessage() + " ===");
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/listings/" + id;
    }
}
