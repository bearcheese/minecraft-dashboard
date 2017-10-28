import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

public class AwsBucketTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsBucketTest.class);

    public static void main(String[] args) {

        S3Client s3Client = S3Client.builder()
                .credentialsProvider(() -> new AwsCredentials("AKIAIGPOP5NQL3DBXCBA", "PI6VUR//fBWwlKyBdflUZhF3QPK3jwV2FnoENPMx"))
                .region(Region.EU_CENTRAL_1)
                .build();
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket("murmur-settings")
                .prefix("spi")
                .delimiter("-")
                .build();
        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        LOGGER.info("Response: {}", response);

        response.contents().forEach(s3Object -> LOGGER.info("S3Object {}", s3Object));
    }
}
