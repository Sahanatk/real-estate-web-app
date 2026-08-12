package com.example.myOwnRealtorWebsite.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class fileUploadService {

    private final Cloudinary cloudinary;

    public fileUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public List<String> uploadPhotos(MultipartFile[] files) throws IOException {
        List<String> photoUrls = new ArrayList<>();

        if (files == null || files.length == 0) return photoUrls;

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) continue;

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "listings",
                            "resource_type", "image",
                            "quality", "auto",
                            "fetch_format", "auto"
                    )
            );

            String url = (String) uploadResult.get("secure_url");
            if (url != null) photoUrls.add(url);
        }

        return photoUrls;
    }

    public void deletePhoto(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            System.err.println("Failed to delete from Cloudinary: " + e.getMessage());
        }
    }
}