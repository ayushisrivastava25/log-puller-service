package com.log.puller.config.properties;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.log.puller.config.S3ClientConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.ConcurrentHashMap;

@ConfigurationProperties("aws")
@Getter
@Setter
public class S3Properties {

    private S3ClientConfig config;

    private AmazonS3 client;

    public AmazonS3 getClient() {

                    return AmazonS3ClientBuilder.standard()
                            .withCredentials(
                                    new AWSStaticCredentialsProvider(
                                            new AWSCredentials() {
                                                @Override
                                                public String getAWSSecretKey() {
                                                    return config.getSecretKey();
                                                }

                                                @Override
                                                public String getAWSAccessKeyId() {
                                                    return config.getAccessKey();
                                                }
                                            }))
                            .withRegion(Regions.valueOf(config.getRegion()))
                            .build();
                }
}