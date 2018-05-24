package com.bpmericle.assetmanager;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Brian Mericle
 */
@Configuration
public class AssetManagerConfiguration {

    /**
     * The AWS region to connect to.
     */
    @Value("#{systemProperties['AWS_S3_REGION']}")
    private String region;

    /**
     * The AWS access key id used to authenticate.
     */
    @Value("#{systemProperties['AWS_ACCESS_KEY_ID']}")
    private String accessKeyId;

    /**
     * The AWS secret access key used to authenticate.
     */
    @Value("#{systemProperties['AWS_SECRET_ACCESS_KEY']}")
    private String secretAccessKey;

    /**
     * Creates and returns an S3 client object used to interact with the S3
     * service in AWS.
     *
     * @return an S3 client
     */
    @Bean("s3client")
    public AmazonS3 getS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }
}
