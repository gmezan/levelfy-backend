package com.uc.backend.aws;

import org.springframework.beans.factory.annotation.Value;


public enum BucketName {

    BUCKET_NAME(BucketHelper.bucketName),
    BUCKET_URL(BucketHelper.bucketUrl);


    private final String value;

    BucketName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

class BucketHelper {
    @Value("${bucket.name}")
    public static String bucketName;

    @Value("${bucket.url}")
    public static String bucketUrl;
}