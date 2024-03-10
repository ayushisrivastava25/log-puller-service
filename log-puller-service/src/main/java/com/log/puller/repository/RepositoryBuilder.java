package com.log.puller.repository;

import com.log.puller.config.properties.S3Properties;
import com.log.puller.repository.impl.S3RepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Import({S3Properties.class})
@Component
public class RepositoryBuilder {

    @Bean
    public S3Repository s3Repository(S3Properties s3Properties) {
        return new S3RepositoryImpl(s3Properties);
    }
}
