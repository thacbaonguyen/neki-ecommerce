package com.thacbao.neki.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.services.CloudinaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));

        log.info("Cloudinary service initialized with cloud name: {}", cloudName);
    }

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        validateImage(file);

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "auto",
                            "quality", "auto:good",
                            "fetch_format", "auto"
                    ));

            String secureUrl = (String) uploadResult.get("secure_url");
            log.info("Image uploaded successfully to: {}", secureUrl);
            return secureUrl;

        } catch (IOException e) {
            log.error("Failed to upload image: {}", e.getMessage(), e);
            throw new InvalidException("Không thể upload ảnh: " + e.getMessage());
        }
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> files, String folder) {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String url = uploadImage(file, folder);
                urls.add(url);
            } catch (Exception e) {
                log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
                // Continue with other images
            }
        }

        return urls;
    }

    @Override
    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId != null) {
                Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                log.info("Image deleted: {} - Result: {}", publicId, result.get("result"));
            }
        } catch (IOException e) {
            log.error("Failed to delete image: {}", imageUrl, e);
            // Don't throw exception for delete failures
        }
    }

    @Override
    public void deleteImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        for (String imageUrl : imageUrls) {
            deleteImage(imageUrl);
        }
    }

    @Override
    public String extractPublicId(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }

        try {
            // URL format: https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{folder}/{filename}.{ext}
            // Public ID: {folder}/{filename}

            String[] parts = imageUrl.split("/upload/");
            if (parts.length < 2) {
                return null;
            }

            String path = parts[1];

            // Remove version number (v1234567890)
            path = path.replaceFirst("v\\d+/", "");

            // Remove file extension
            int lastDot = path.lastIndexOf('.');
            if (lastDot > 0) {
                path = path.substring(0, lastDot);
            }

            return path;

        } catch (Exception e) {
            log.error("Failed to extract public ID from URL: {}", imageUrl, e);
            return null;
        }
    }

    // Helper method to validate image file
    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidException("File ảnh không được để trống");
        }

        // Check file size (max 10MB)
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            throw new InvalidException("Kích thước file không được vượt quá 10MB");
        }

        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidException("File phải là ảnh (jpg, png, webp, etc.)");
        }

        // Check allowed image types
        List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/webp", "image/jpg");
        if (!allowedTypes.contains(contentType.toLowerCase())) {
            throw new InvalidException("Chỉ chấp nhận file ảnh định dạng: JPG, PNG, WEBP");
        }
    }
}