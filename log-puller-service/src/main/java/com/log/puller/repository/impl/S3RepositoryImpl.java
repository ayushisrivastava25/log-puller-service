package com.log.puller.repository.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.log.puller.config.properties.S3Properties;
import com.log.puller.repository.S3Repository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class S3RepositoryImpl implements S3Repository {

    private S3Properties s3Properties;
    public S3RepositoryImpl(S3Properties s3Properties) {
        this.s3Properties = s3Properties;
    }

    public Map<String, List<String>> listObjects(String bucketName) {
        AmazonS3 s3Client = s3Properties.getClient();

        Map<String, List<String>> objectKeys = new HashMap<>();
        List<String> objects = new ArrayList<>();
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName);

        ListObjectsV2Result result;
System.out.println(ZonedDateTime.now(ZoneId.of(ZoneOffset.UTC.getId())));
        try {
            do {
                result = s3Client.listObjectsV2(request);
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    String key = objectSummary.getKey();
                    List<String> commonPrefixes = result.getCommonPrefixes();
                    for (String prefix : commonPrefixes) {
                        objects.addAll(listObjects(bucketName, prefix));
                    }
                    objectKeys.put(key, objects);
                }
                request.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated());
        } catch (AmazonS3Exception e) {
            log.error(
                    "Error occurred while listing objects : {}", e);
        }

        return objectKeys;
    }

    private List<String> listObjects(String bucketName, String prefix) {
        AmazonS3 s3Client = s3Properties.getClient();

        List<String> objects = new ArrayList<>();
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix);

        ListObjectsV2Result result;

        try {
            do {
                result = s3Client.listObjectsV2(request);
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    if (!objectSummary.getKey().endsWith("/")) {
                        objects.add(objectSummary.getKey());
                    }
                }
                request.setContinuationToken(result.getNextContinuationToken());
            } while (result.isTruncated());
        } catch (AmazonS3Exception e) {
            log.error(
                    "Error occurred while listing objects : {}", e);
        }

        return objects;
    }

    public String getObject(String bucket, String key) {
        AmazonS3 s3Client = s3Properties.getClient();
        String contents = null;
        try {
            contents = s3Client.getObject(bucket, key).getObjectContent().readAllBytes().toString();
        } catch (IOException e) {
            log.error("Error occurred while reading file contents");
        }
        return contents;
    }
}
