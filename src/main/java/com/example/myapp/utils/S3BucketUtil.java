package com.example.myapp.utils;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

/**
 * Provides functions related to S3 bucket.
 *
 * @author Dinh Nguyen Hai Dang
 * @version 1.1
 * @since 18-Aug-2021
 */
public class S3BucketUtil {

    /**
     * Create bucket.
     *
     * @param s3Client
     * @param bucketName
     * @param region
     */
    public static void createBucket(S3Client s3Client, String bucketName, Region region) {
        try {
            // Create bucket.
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder().bucket(bucketName)
                    .createBucketConfiguration(CreateBucketConfiguration.builder()
                            .locationConstraint(region.id()).build())
                    .build();
            System.out.println("Creating bucket: " + bucketName);
            s3Client.createBucket(bucketRequest);

            // Wait until the bucket is created.
            s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder().bucket(bucketName).build());
            System.out.println(bucketName + " is ready.");
            System.out.printf("%n");
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    /**
     * Delete bucket.
     *
     * @param s3Client
     * @param bucketName
     */
    public static void deleteBucket(S3Client s3Client, String bucketName) {
        try {
            // To delete a bucket, all the objects in the bucket must be deleted first.
            S3ObjectUtil.deleteAllS3Objects(s3Client, bucketName);

            // Delete the bucket.
            System.out.println("Deleting bucket: " + bucketName);
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucketName).build();
            s3Client.deleteBucket(deleteBucketRequest);
            System.out.println(bucketName + " has been deleted.");
            System.out.printf("%n");
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
