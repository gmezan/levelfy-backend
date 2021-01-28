package com.uc.backend.service.aws;

import com.uc.backend.aws.BucketName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class AwsResourceService {

    private final static Logger logger = LoggerFactory.getLogger(AwsResourceService.class);
    
    private final FileStore fileStore;


    @Autowired
    public AwsResourceService(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    // Return url
    public String uploadImage(String name, String folder, MultipartFile file) throws IllegalAccessException, IOException {
        // 1. Check if image is not empty
        if (file.isEmpty())
            throw new IllegalStateException("Cannot upload empty file ["+file.getSize()+"]");
        // 2. If file is an image
        if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType()).contains(file.getContentType()))
            throw new IllegalAccessException("File must be an image");

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        // 5. Store the image in s3 and update database with s3 image link
        String path = String.format("%s/%s/%s", "levelfy-development-private", folder, name );

        String fileName = String.format("%s-%s", file.getName(), UUID.randomUUID());
        fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream()) ;

        return String.format("%s/%s/%s/%s", BucketName.BUCKET_URL.getValue(), folder, name, fileName);

    }

}