package com.log.puller.config;

import lombok.Data;
import lombok.Getter;

@Data
public class S3ClientConfig {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private String envFolder;
}