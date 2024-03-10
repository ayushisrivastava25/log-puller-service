package com.log.puller.service;

import com.log.puller.config.S3ClientConfig;
import com.log.puller.config.properties.S3Properties;
import com.log.puller.repository.S3Repository;
import com.log.puller.service.impl.GrepSearchServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Import({S3Properties.class})
@Component
public class ServiceBuilder {

    @Bean
    public GrepSearchService grepSearchService(S3Properties s3Properties, S3Repository s3Repository) {
        return new GrepSearchServiceImpl(s3Properties, s3Repository);
    }
}
