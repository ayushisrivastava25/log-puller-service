package com.log.puller.repository;

import java.util.List;
import java.util.Map;

public interface S3Repository {

    Map<String, List<String>> listObjects(String bucketName);

    String getObject(String bucket, String key);
}
