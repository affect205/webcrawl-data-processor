package org.alexside.webcrawl.modules.loader.impl.s3.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class S3WebcrawlLoaderConfig {
    private final S3WebcrawlLoaderProps props;

    @Bean
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder// Get AmazonS3 client and return the s3Client object.
                .standard()
                .withRegion(Regions.fromName(props.getAws().getS3Region()))
//                .withCredentials(new AWSStaticCredentialsProvider(
//                        new BasicAWSCredentials(props.getAws().getAccessKeyId(), props.getAws().getSecretAccessKey())
//                ))
                .build();
    }

    @Data
    @Configuration
    @ConfigurationProperties("webcrawl.loader")
    public static class S3WebcrawlLoaderProps {
        private AWSS3Props aws;
        @Data @NoArgsConstructor
        @AllArgsConstructor
        public static class AWSS3Props {
            private String accessKeyId, secretAccessKey, s3Bucket, s3Region;
        }
    }
}