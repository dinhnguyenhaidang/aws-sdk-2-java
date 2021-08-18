package com.example.myapp;

import com.example.myapp.utils.S3BucketUtil;
import com.example.myapp.utils.S3ObjectUtil;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class App {
    public static void main(String[] args) {
        Region region = Region.AP_SOUTHEAST_1;
        S3Client s3Client = S3Client.builder().region(region).build();
        String bucketName = "bucket" + System.currentTimeMillis();
        String keyName = "key";

        S3BucketUtil.createBucket(s3Client, bucketName, region);
        S3ObjectUtil.putS3Object(s3Client, bucketName, keyName);
        S3BucketUtil.deleteBucket(s3Client, bucketName);

        System.out.println("Closing the connection to {S3}");
        s3Client.close();
        System.out.println("Connection closed");
        System.out.println("Exiting...");
    }
}