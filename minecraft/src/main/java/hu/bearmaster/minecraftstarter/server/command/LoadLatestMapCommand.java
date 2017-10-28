package hu.bearmaster.minecraftstarter.server.command;

import static hu.bearmaster.minecraftstarter.server.model.Action.LOAD_MAP;
import static hu.bearmaster.minecraftstarter.server.model.ServerStatus.RUNNING;
import static hu.bearmaster.minecraftstarter.server.model.ResponseStatus.FAILED;
import static hu.bearmaster.minecraftstarter.server.model.ResponseStatus.SUCCESSFUL;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.bearmaster.minecraftstarter.server.model.Action;
import hu.bearmaster.minecraftstarter.server.model.CommandDetails;
import hu.bearmaster.minecraftstarter.server.model.ExecutionResponse;
import hu.bearmaster.minecraftstarter.server.model.MapName;
import hu.bearmaster.minecraftstarter.server.model.MinecraftDetails;
import hu.bearmaster.minecraftstarter.server.service.MapNameGeneratorService;
import hu.bearmaster.minecraftstarter.server.service.StatusService;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.sync.ResponseInputStream;

@Component
public class LoadLatestMapCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadLatestMapCommand.class);

    @Value("${aws.saves.bucket.name}")
    private String bucket;

    @Value("${minecraft.root.path}")
    private File minecraftRootDir;

    private final MapNameGeneratorService mapNameGeneratorService;

    private final S3Client s3Client;

    private final StatusService statusService;

    public LoadLatestMapCommand(MapNameGeneratorService mapNameGeneratorService, S3Client s3Client, StatusService statusService) {
        this.mapNameGeneratorService = mapNameGeneratorService;
        this.s3Client = s3Client;
        this.statusService = statusService;
    }

    @Override
    public ExecutionResponse execute(CommandDetails commandDetails) {
        LOGGER.info("Executing {}", commandDetails);
        ExecutionResponse executionResponse = new ExecutionResponse();
        executionResponse.setId(commandDetails.getId());

        if (statusService.getCurrentStatus().getServerDetails().getStatus() == RUNNING) {
            LOGGER.warn("Cannot load map while server is running!");
            executionResponse.setStatus(FAILED);
            executionResponse.setMessage("Cannot load map while server is running!");
            return executionResponse;
        }

        try {
            Optional<MapName> latestEntry = findLatestEntry();

            if (latestEntry.isPresent()) {
                String mapName = mapNameGeneratorService.format(latestEntry.get());
                LOGGER.info("Latest map found as {}", mapName);
                File destination = new File(minecraftRootDir, mapName);
                if (destination.exists()) {
                    destination.delete();
                }
                //TODO should be async from here...
                Map<String, String> metadata = downloadEntryTo(mapName, destination);
                LOGGER.info("Loaded map metadata: {}", metadata);
                unzipFile(destination.getAbsolutePath(), minecraftRootDir.getAbsolutePath());
                MinecraftDetails details = statusService.registerMapLoaded(latestEntry.get());
                executionResponse.setStatus(SUCCESSFUL);
                executionResponse.setMinecraftDetails(details);
            } else {
                LOGGER.warn("Latest map not found");
                executionResponse.setStatus(FAILED);
                executionResponse.setMessage("Saved map not found");
            }
        } catch (IOException | ZipException e) {
            LOGGER.warn("Failed to load latest map", e);
            executionResponse.setStatus(FAILED);
            executionResponse.setMessage(e.getMessage());
        }

        return executionResponse;
    }

    @Override
    public boolean isExecutedOn(Action action) {
        return action == LOAD_MAP;
    }

    private Optional<MapName> findLatestEntry() {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .build();
        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        if (response.contents() != null) {
            return response.contents().stream()
                    .map(s3Object -> mapNameGeneratorService.parse(s3Object.key()))
                    .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                    .max(Comparator.reverseOrder());
        } else {
            return Optional.empty();
        }
    }

    private Map<String, String> downloadEntryTo(String key, File destination) throws IOException {
        LOGGER.info("Downloading {} to {}", key, destination);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> object = s3Client.getObject(getObjectRequest);
        FileUtils.copyInputStreamToFile(object, destination);

        return object.response().metadata();
    }

    private void unzipFile(String pathToFile, String destinationFolder) throws ZipException {
        LOGGER.info("Extracting {} to {}", pathToFile, destinationFolder);
        ZipFile zipFile = new ZipFile(pathToFile);
        zipFile.extractAll(destinationFolder);
    }
}
