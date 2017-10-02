package hu.bearmaster.minecraftstarter.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ProtocolResolver;

import hu.bearmaster.minecraftstarter.shared.aws.S3ProtocolResolver;
import software.amazon.awssdk.auth.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsClientConfiguration {

    @Autowired
    public AwsCredentialsProvider credentialsProvider;

    @Autowired
    public Region region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build();
    }

    @Bean
    public ProtocolResolver s3ProtocolResolver() {
        return new S3ProtocolResolver(s3Client());
    }

    @Autowired
    public void registerS3ProtocolResolver(GenericApplicationContext context) {
        context.addProtocolResolver(s3ProtocolResolver());
    }
}
