package hu.bearmaster.minecraftstarter.server.command;

import static hu.bearmaster.minecraftstarter.server.model.Action.SAVE_MAP;
import static hu.bearmaster.minecraftstarter.server.model.ResponseStatus.FAILED;
import static hu.bearmaster.minecraftstarter.server.model.ResponseStatus.SUCCESSFUL;

import java.io.File;
import java.time.LocalDate;

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
import net.lingala.zip4j.model.ZipParameters;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class SaveMapCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveMapCommand.class);

    @Value("${aws.saves.bucket.name}")
    private String bucket;

    @Value("${minecraft.root.path}")
    private File minecraftRootDir;

    @Value("${minecraft.world.name}")
    private String minecraftWorldName;

    private final MapNameGeneratorService mapNameGeneratorService;
    
    private final StatusService statusService;

    private final S3Client s3Client;

    public SaveMapCommand(MapNameGeneratorService mapNameGeneratorService, StatusService statusService, S3Client s3Client) {
        this.mapNameGeneratorService = mapNameGeneratorService;
        this.statusService = statusService;
        this.s3Client = s3Client;
    }

    @Override
    public ExecutionResponse execute(CommandDetails commandDetails) {
        LOGGER.info("Executing {}", commandDetails);
        ExecutionResponse executionResponse = new ExecutionResponse();
        executionResponse.setId(commandDetails.getId());

        //TODO check if minecraft server is running
        File minecraftWorldDir = new File(minecraftRootDir, minecraftWorldName);
        if (minecraftWorldDir.exists() && minecraftWorldDir.isDirectory()) {
            try {
                //TODO should be async from here?
                MapName savedMapName = mapNameGeneratorService.generate(LocalDate.now());
                File zippedMap = zipCurrentMap(minecraftWorldDir, savedMapName);
                uploadMapToS3(zippedMap);
                MinecraftDetails details = statusService.registerMapSaved(savedMapName);
                executionResponse.setStatus(SUCCESSFUL);
                executionResponse.setMessage("Successfully saved " + zippedMap.getName());
                executionResponse.setMinecraftDetails(details);
            } catch (ZipException e) {
                LOGGER.warn("Failed to save current map", e);
                executionResponse.setStatus(FAILED);
                executionResponse.setMessage(e.getMessage());
            }
        } else {
            LOGGER.warn("Minecraft world '{}' not found", minecraftWorldDir);
            executionResponse.setStatus(FAILED);
            executionResponse.setMessage("Minecraft world '" + minecraftWorldName + "' does not exist");
        }

        return executionResponse;
    }

    private File zipCurrentMap(File minecraftWorldDir, MapName savedMapName) throws ZipException {
        File destination = new File(minecraftRootDir, mapNameGeneratorService.format(savedMapName));
        ZipFile zipFile = new ZipFile(destination);
        ZipParameters parameters = new ZipParameters();
        zipFile.addFolder(minecraftWorldDir, parameters);
        return destination;
    }

    private void uploadMapToS3(File zippedMapFile) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(zippedMapFile.getName())
                .build();
        s3Client.putObject(request, zippedMapFile.toPath());
    }

    @Override
    public boolean isExecutedOn(Action action) {
        return action == SAVE_MAP;
    }
}
