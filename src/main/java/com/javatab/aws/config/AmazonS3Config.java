package com.javatab.aws.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${awsS3.accessKey}")
    private String accessKey;

    @Value("${awsS3.secretKey}")
    private String secretKey;

    @Value("${awsS3.region}")
    private String region;

    private AmazonS3 amazonS3Client() {

        AWSCredentials awsCredentials =
                new BasicAWSCredentials(this.accessKey, this.secretKey);

        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
