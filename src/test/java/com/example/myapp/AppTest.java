package com.example.myapp;

import com.example.myapp.utils.S3BucketUtil;
import com.example.myapp.utils.S3ObjectUtil;
import org.junit.jupiter.api.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {
    private static S3Client s3;
    private static Region region;

    // Define the data members required for the tests
    private static String bucketName = "";
    private static String objectKey = "";

    @BeforeAll
    public static void setUp() {
        // Run tests on real AWS resources.
        region = Region.AP_SOUTHEAST_1;
        s3 = S3Client.builder().region(region).build();

        try (InputStream input = AppTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            // Load a properties file from class path, inside static method.
            prop.load(input);

            // Populate the data members required for all tests
            bucketName = prop.getProperty("bucketName");
            objectKey = prop.getProperty("objectKey");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void whenInitializingAWSS3Service_thenNotNull() {
        assertNotNull(s3);
        System.out.println("Test 1 passed");
    }

    @Test
    @Order(2)
    public void createBucket() {
        S3BucketUtil.createBucket(s3, bucketName, region);
        System.out.println("Test 2 passed");
    }

    /**
     *
     */
    @Test
    @Order(3)
    public void putObject() {
        String result = S3ObjectUtil.putS3Object(s3, bucketName, objectKey);
        assertFalse(result.isEmpty());
        System.out.println("Test 3 passed");
    }

    /**
     *
     */
    @Test
    @Order(4)
    public void deleteObject() {
        S3ObjectUtil.deleteS3Object(s3, bucketName, objectKey);
        System.out.println("Test 4 passed");
    }

    @Test
    @Order(5)
    public void deleteBucket() {
        S3BucketUtil.deleteBucket(s3, bucketName);
        System.out.println("Test 5 passed");
    }
}
