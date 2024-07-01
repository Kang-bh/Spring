package com.filespace.domain.space.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3BucketCreator {
    private final AmazonS3Client amazonS3Client;

    public String createBucket() {
        String bucketName = generateBucketName();

        while (true) { // todo : statement 정리
            if (!amazonS3Client.doesBucketExistV2(bucketName)) {
                Bucket bucket = amazonS3Client.createBucket(bucketName);
                System.out.println("Bucket created: " + bucket.getName());
                return bucketName;
            }
        }

    }

    private static String generateBucketName() {
        String uuid = UUID.randomUUID().toString();

        String bucketName = "space-cloud-" + uuid;
        System.out.println(bucketName);
        return bucketName;
    }
}
