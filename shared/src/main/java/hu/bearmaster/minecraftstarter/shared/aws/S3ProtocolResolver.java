package hu.bearmaster.minecraftstarter.shared.aws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.sync.ResponseInputStream;

public class S3ProtocolResolver implements ProtocolResolver {

    private static final String S3_PROTOCOL_PREFIX = "s3://";

    private final S3Client s3Client;

    public S3ProtocolResolver(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        return location.startsWith(S3_PROTOCOL_PREFIX) ? loadS3Object(location) : null;
    }

    private Resource loadS3Object(String location) {
        String[] bucketAndKey = resolveBucketAndObjectKey(location);

        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketAndKey[0])
                .key(bucketAndKey[1])
                .build();

        HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
        return new S3Resource(bucketAndKey[0], bucketAndKey[1], headObjectResponse, objectLoader(location));
    }

    private Supplier<InputStream> objectLoader(String location) {
        return () -> {
            String[] bucketAndKey = resolveBucketAndObjectKey(location);
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketAndKey[0])
                    .key(bucketAndKey[1])
                    .build();
            ResponseInputStream<GetObjectResponse> object = s3Client.getObject(getObjectRequest);
            return object;
        };
    }

    private String[] resolveBucketAndObjectKey(String location) {
        String[] parts = location.substring(S3_PROTOCOL_PREFIX.length()).split("/");
        String bucket = parts[0];
        String key = Arrays.stream(Arrays.copyOfRange(parts, 1, parts.length)).collect(Collectors.joining("/"));
        return new String[]{bucket, key};
    }

    public static class S3Resource extends AbstractResource {

        private final HeadObjectResponse headObjectResponse;

        private final Supplier<InputStream> objectLoader;

        private final String bucket;

        private final String key;

        public S3Resource(String bucket, String key, HeadObjectResponse headObjectResponse, Supplier<InputStream> objectLoader) {
            this.headObjectResponse = headObjectResponse;
            this.objectLoader = objectLoader;
            this.bucket = bucket;
            this.key = key;
        }

        @Override
        public boolean exists() {
            return headObjectResponse != null;
        }

        @Override
        public URL getURL() throws IOException {
            return new URL("http://" + bucket + ".s3.amazonaws.com/" + key);
        }

        @Override
        public File getFile() throws IOException {
            throw new UnsupportedOperationException("Amazon S3 resource can not be resolved to java.io.File objects.Use " +
                    "getInputStream() to retrieve the contents of the object!");
        }

        @Override
        public long contentLength() throws IOException {
            return headObjectResponse != null ? headObjectResponse.contentLength() : 0;
        }

        @Override
        public long lastModified() throws IOException {
            return headObjectResponse != null ? headObjectResponse.lastModified().getEpochSecond() : 0;
        }

        @Override
        public Resource createRelative(String s) throws IOException {
            throw new UnsupportedOperationException("Amazon S3 resource cannot support relate object creation");
        }

        @Override
        public String getFilename() {
            return key;
        }

        @Override
        public String getDescription() {
            return "AWS S3 Object[bucket=" + bucket + ", key=" + key + "]";
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return objectLoader.get();
        }
    }
}