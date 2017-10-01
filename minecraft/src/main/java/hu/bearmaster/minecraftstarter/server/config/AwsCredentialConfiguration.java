package hu.bearmaster.minecraftstarter.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.auth.AwsCredentials;
import software.amazon.awssdk.auth.AwsCredentialsProvider;
import software.amazon.awssdk.auth.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;

@Configuration
public class AwsCredentialConfiguration {

    @Value("${aws.accessKey:undefined}")
    private String awsAccessKey;

    @Value("${aws.secretKey:undefined}")
    private String awsSecretKey;

    @Value("${aws.region:eu-central-1}")
    private String awsRegionName;

    @Bean
    @Profile("!production")
    public AwsCredentialsProvider credentialsProvider() {
        return () -> new AwsCredentials(awsAccessKey, awsSecretKey);
    }

    @Bean
    @Profile("production")
    public AwsCredentialsProvider instanceProfileCredentialProvider() {
        return InstanceProfileCredentialsProvider.builder()
                .build();
    }

    @Bean
    public Region region() {
        return Region.of(awsRegionName);
    }

}
