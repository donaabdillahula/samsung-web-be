package com.dona_samsung_web_project.samsung_web_be.storage;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import io.imagekit.sdk.utils.Utils;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Service
public class ImageKitStorageService {

    private ImageKit imageKit;

    @PostConstruct
    public void init() {
        imageKit = ImageKit.getInstance();
        try {
            Configuration config  = Utils.getSystemConfig(ImageKitStorageService.class);
            imageKit.setConfig(config);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            // Optionally, handle the error or exit
        }
    }

    public String uploadFile(MultipartFile file, String fileName, String folder) throws Exception {
        // Convert file to Base64 string
        String base64 = Base64.getEncoder().encodeToString(file.getBytes());

        // Create upload request
        FileCreateRequest request = new FileCreateRequest(base64, fileName);
        request.setFolder(folder); // e.g. "/books"
        request.setUseUniqueFileName(true);

        // Upload file
        Result result = imageKit.upload(request);
        if (result.getUrl() == null || result.getUrl().isEmpty()) {
            throw new RuntimeException("ImageKit upload failed: ");
        }

        // Return public URL
        return result.getUrl();
    }

    public void deleteFile(String fileId) throws Exception {
        imageKit.deleteFile(fileId);
    }
}
