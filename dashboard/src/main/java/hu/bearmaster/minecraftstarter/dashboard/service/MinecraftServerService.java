package hu.bearmaster.minecraftstarter.dashboard.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import hu.bearmaster.minecraftstarter.dashboard.domain.Ec2Instance;
import hu.bearmaster.minecraftstarter.dashboard.domain.ExecutionRequest;
import hu.bearmaster.minecraftstarter.dashboard.domain.ExecutionResponse;
import hu.bearmaster.minecraftstarter.dashboard.domain.MinecraftServerInfo;
import hu.bearmaster.minecraftstarter.dashboard.domain.User;

@Service
public class MinecraftServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftServerService.class);

    @Value("${aws.asg.name}")
    private String autoScalingGroupName;

    @Value("${minecraft.server.port}")
    private int minecraftServerPort;

    private final AwsService awsService;

    private final JwtGeneratorService jwtGeneratorService;

    private final RestTemplate restTemplate;

    public MinecraftServerService(AwsService awsService, JwtGeneratorService jwtGeneratorService, RestTemplate restTemplate) {
        this.awsService = awsService;
        this.jwtGeneratorService = jwtGeneratorService;
        this.restTemplate = restTemplate;
    }

    public List<Ec2Instance> getServerList(User user) {
        List<String> instanceIds = awsService.getInstancesIdsOfAutoScalingGroup(autoScalingGroupName);
        List<Ec2Instance> instances = awsService.getInstancesByIds(instanceIds);

        enrichInstancesWithMinecraftServerInfo(user, instances);

        return instances;
    }

    public void startUpServerInstance() {
        LOGGER.info("Starting instance in {} group", autoScalingGroupName);
        awsService.upscaleAutoScalingGroup(autoScalingGroupName);
    }

    public void stopServerInstance() {
        //TODO add failsafe checks for saving the world map
        LOGGER.info("Stopping instance in {} group", autoScalingGroupName);
        awsService.downscaleAutoScalingGroup(autoScalingGroupName);
    }

    private void enrichInstancesWithMinecraftServerInfo(User user, List<Ec2Instance> instances) {
        for (Ec2Instance instance : instances) {
            if (instance.getState().equals("running")) {
                try {
                    ExecutionResponse response = getServerStatus(user, instance.getPublicIpAddress());
                    instance.setMinecraftServerInfo(new MinecraftServerInfo(response.getAdditionalInfo()));
                } catch (Exception e) {
                    LOGGER.warn("Failed to connect to minecraft service on {} ({})", instance.getPublicIpAddress(), e.getMessage());
                    MinecraftServerInfo minecraftServerInfo = new MinecraftServerInfo();
                    minecraftServerInfo.setStatus("not running");
                    instance.setMinecraftServerInfo(minecraftServerInfo);
                }
            }
        }
    }

    private ExecutionResponse getServerStatus(User user, String publicIpAddress) {
        ExecutionRequest request = generateStatusRequest(user);
        String requestBody = jwtGeneratorService.generateSignedRequest(request);
        ResponseEntity<ExecutionResponse> responseEntity = restTemplate.postForEntity(getExecuteUrl(publicIpAddress), requestBody, ExecutionResponse.class);
        LOGGER.info("Response: {}", responseEntity.getBody());
        return responseEntity.getBody();
    }

    private ExecutionRequest generateStatusRequest(User user) {
        ExecutionRequest request = new ExecutionRequest();
        String uuid = UUID.randomUUID().toString();
        request.setId(uuid);
        request.setSubject("status");
        request.setRequestor(user.getEmail());
        return request;
    }

    private String getExecuteUrl(String publicIpAddress) {
        return "http://" + publicIpAddress + ":" + minecraftServerPort + "/minecraft/execute";
    }

}
