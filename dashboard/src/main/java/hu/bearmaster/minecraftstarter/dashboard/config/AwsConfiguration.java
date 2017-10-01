package hu.bearmaster.minecraftstarter.dashboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ProtocolResolver;

import hu.bearmaster.minecraftstarter.shared.aws.S3ProtocolResolver;
import software.amazon.awssdk.auth.AwsCredentials;
import software.amazon.awssdk.auth.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.ec2.EC2Client;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfiguration {

    @Value("${aws.accessKey:undefined}")
    private String awsAccessKey;

    @Value("${aws.secretKey:undefined}")
    private String awsSecretKey;

    @Value("${aws.region:eu-central-1}")
    private String awsRegionName;

    @Bean
    public AwsCredentialsProvider credentialsProvider() {
        return () -> new AwsCredentials(awsAccessKey, awsSecretKey);
    }

    @Bean
    public AutoScalingClient autoScalingClient() {
        return AutoScalingClient.builder()
                .region(region())
                .credentialsProvider(credentialsProvider())
                .build();
    }

    @Bean
    public EC2Client ec2Client() {
        return EC2Client.builder()
                .region(region())
                .credentialsProvider(credentialsProvider())
                .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(credentialsProvider())
                .region(region())
                .build();
    }

    private Region region() {
        return Region.of(awsRegionName);
    }

    @Bean
    public ProtocolResolver s3ProtocolResolver() {
        return new S3ProtocolResolver(s3Client());
    }
}
