package com.example.myapp.utils;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

/**
 * Provides functions related to S3 object.
 *
 * @author Dinh Nguyen Hai Dang
 * @version 1.1
 * @since 18-Aug-2021
 */
public class S3ObjectUtil {

    /**
     * Create a S3 object in a S3 bucket.
     *
     * @param s3Client
     * @param bucketName
     * @param keyName
     */
    public static String putS3Object(S3Client s3Client, String bucketName, String keyName) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(keyName).build();

            System.out.println("Uploading object...");
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
                    RequestBody.fromString("Testing with the {sdk-java}"));
            System.out.println("Upload complete");
            System.out.printf("%n");

            return putObjectResponse.eTag();
        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    /**
     * Delete a S3 object n a S3 bucket.
     *
     * @param s3Client
     * @param bucketName
     * @param keyName
     */
    public static void deleteS3Object(S3Client s3Client, String bucketName, String keyName) {
        try {
            System.out.println("Deleting object: " + keyName);
            DeleteObjectRequest deleteObjectRequest =
                    DeleteObjectRequest.builder().bucket(bucketName).key(keyName).build();
            s3Client.deleteObject(deleteObjectRequest);
            System.out.println(keyName + " has been deleted.");
            System.out.printf("%n");
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    /**
     * Delete all S3 objects in a S3 bucket.
     *
     * @param s3Client
     * @param bucketName
     */
    public static void deleteAllS3Objects(S3Client s3Client, String bucketName) {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName).build();
        ListObjectsV2Response listObjectsV2Response;

        do {
            listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
            for (S3Object s3Object : listObjectsV2Response.contents()) {
                s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Object.key())
                        .build());
            }
            listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName)
                    .continuationToken(listObjectsV2Response.nextContinuationToken())
                    .build();
        } while (listObjectsV2Response.isTruncated());
    }

}
