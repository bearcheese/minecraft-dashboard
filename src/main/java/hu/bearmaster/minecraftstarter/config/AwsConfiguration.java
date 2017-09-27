package hu.bearmaster.minecraftstarter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import software.amazon.awssdk.auth.AwsCredentials;
import software.amazon.awssdk.auth.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.ec2.EC2Client;

@Configuration
public class AwsConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public AwsCredentialsProvider credentialsProvider() {
        String accessKey = env.getRequiredProperty("aws.accessKey");
        String secretKey = env.getRequiredProperty("aws.secretKey");
        return () -> new AwsCredentials(accessKey, secretKey);
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

    private Region region() {
        return Region.of(env.getRequiredProperty("aws.region"));
    }
}
