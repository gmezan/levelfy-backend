package com.uc.backend.service.aws;

import com.uc.backend.dto.FileS3ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class AwsResourceService {

    private final static Logger logger = LoggerFactory.getLogger(AwsResourceService.class);
    private final String BUCKET_NAME;
    private final String BUCKET_URL;

    private final Environment env;
    private final FileStore fileStore;


    @Autowired
    public AwsResourceService(FileStore fileStore, Environment env) {
        this.fileStore = fileStore;
        this.env = env;
        this.BUCKET_NAME = this.env.getProperty("bucket.name");
        this.BUCKET_URL = this.env.getProperty("bucket.url");
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
        String path = String.format("%s/%s/%s", BUCKET_NAME, folder, name );

        String fileName = String.format("%s-%s", file.getName(), UUID.randomUUID());
        fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream()) ;

        return String.format("%s/%s/%s/%s", BUCKET_URL, folder, name, fileName);

    }

    public FileS3ResponseDto uploadFile(String subFolder, String folder, MultipartFile file) throws IllegalAccessException, IOException {
        FileS3ResponseDto responseDto = new FileS3ResponseDto();

        if (file.isEmpty())
            throw new IllegalStateException("Cannot upload empty file ["+file.getSize()+"]");
        // 2. If file is an image
        if (!Arrays.asList(IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType(),
                TEXT_PLAIN.getMimeType(),
                "application/pdf"
                ).contains(file.getContentType()))
            throw new IllegalAccessException("File must be a valid type");

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        // 5. Store the image in s3 and update database with s3 image link - SERVICE ID
        String path = String.format("%s/%s/%s", BUCKET_NAME, folder, subFolder );

        String fileName = String.format("%s-%s", UUID.randomUUID(), file.getOriginalFilename());
        fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());

        responseDto.setFileUrl(String.format("%s/%s/%s/%s", BUCKET_URL, folder, subFolder, fileName));
        responseDto.setFileName(file.getOriginalFilename());

        return responseDto;
    }
}
