package com.uc.backend.aws;

import org.springframework.beans.factory.annotation.Value;


public enum BucketName {

    BUCKET_NAME("levelfy-development-private"),
    BUCKET_URL("https://levelfy-development-private.s3-sa-east-1.amazonaws.com");


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