package com.uc.backend.service.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void save(String path, String fileName, Optional<Map<String, String>> optionalMetadata, InputStream inputStream) {

        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map->{
            if (!map.isEmpty()) {
                map.forEach(metadata::addUserMetadata);
            }
        });

        try {
            s3.putObject(new PutObjectRequest(path, fileName, inputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
        }
        catch (AmazonServiceException ex) {
            throw new IllegalStateException("Failed to store file to s3");
        }

    }

}
