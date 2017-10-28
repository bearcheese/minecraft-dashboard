package hu.bearmaster.minecraftstarter.dashboard.service;

import static hu.bearmaster.minecraftstarter.dashboard.domain.Action.LOAD_MAP;
import static hu.bearmaster.minecraftstarter.dashboard.domain.Action.SAVE_MAP;
import static hu.bearmaster.minecraftstarter.dashboard.domain.Action.START_SERVER;
import static hu.bearmaster.minecraftstarter.dashboard.domain.Action.STATUS;
import static hu.bearmaster.minecraftstarter.dashboard.domain.Action.STOP_SERVER;
import static hu.bearmaster.minecraftstarter.dashboard.domain.response.ResponseStatus.FAILED;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import hu.bearmaster.minecraftstarter.dashboard.domain.Action;
import hu.bearmaster.minecraftstarter.dashboard.domain.ServerInstance;
import hu.bearmaster.minecraftstarter.dashboard.domain.ExecutionRequest;
import hu.bearmaster.minecraftstarter.dashboard.domain.User;
import hu.bearmaster.minecraftstarter.dashboard.domain.response.ExecutionResponse;

public abstract class AbstractMinecraftServerService implements MinecraftServerService {

    @Value("${minecraft.server.port}")
    private int minecraftServerPort;

    protected final RequestGeneratorService requestGeneratorService;

    protected final RestTemplate restTemplate;

    protected AbstractMinecraftServerService(RequestGeneratorService requestGeneratorService, RestTemplate restTemplate) {
        this.requestGeneratorService = requestGeneratorService;
        this.restTemplate = restTemplate;
    }

    @Override
    public ExecutionResponse loadLatestMap(User user) {
        return executeAction(user, LOAD_MAP);
    }

    @Override
    public ExecutionResponse saveCurrentMap(User user) {
        return executeAction(user, SAVE_MAP);
    }

    @Override
    public ExecutionResponse startMinecraftServer(User user) {
        return executeAction(user, START_SERVER);
    }

    @Override
    public ExecutionResponse stopMinecraftServer(User user) {
        return executeAction(user, STOP_SERVER);
    }

    @Override
    public Optional<ServerInstance> getServerStatus(User user) {
        Optional<ServerInstance> ec2Instance = getActiveServer(user);
        ec2Instance.ifPresent(instance -> enrichInstancesWithMinecraftServerInfo(user, instance));
        return ec2Instance;
    }

    protected abstract Logger getLogger();

    protected void enrichInstancesWithMinecraftServerInfo(User user, ServerInstance instance) {
        try {
            ExecutionResponse response = getServerStatus(user, instance);
            instance.setMinecraftDetails(response.getMinecraftDetails());
        } catch (Exception e) {
            getLogger().warn("Failed to connect to minecraft service on {} ({})", instance.getPublicIpAddress(), e.getMessage());
        }
    }

    private ExecutionResponse getServerStatus(User user, ServerInstance instance) {
        ExecutionRequest request = generateRequestWithAction(user, STATUS);
        String requestBody = requestGeneratorService.generateSignedRequest(request);
        ResponseEntity<ExecutionResponse> responseEntity = restTemplate.postForEntity(getExecuteUrl(instance.getPublicIpAddress()), requestBody, ExecutionResponse.class);
        getLogger().info("Response: {}", responseEntity.getBody());
        return responseEntity.getBody();
    }

    private ExecutionResponse executeAction(User user, Action action) {
        getLogger().info("Executing action {}", action);
        Optional<ServerInstance> ec2Instance = getActiveServer(user);
        if (ec2Instance.isPresent()) {
            ExecutionRequest request = generateRequestWithAction(user, action);
            String signedRequest = requestGeneratorService.generateSignedRequest(request);
            String executeUrl = getExecuteUrl(ec2Instance.get().getPublicIpAddress());
            getLogger().info("Sending request: {}", request);
            ExecutionResponse executionResponse = restTemplate.postForEntity(executeUrl, signedRequest, ExecutionResponse.class).getBody();
            getLogger().info("Response: {}", executionResponse);
            return executionResponse;
        } else {
            ExecutionResponse response = new ExecutionResponse();
            response.setStatus(FAILED);
            response.setMessage("Server instance is not running");
            return response;
        }
    }

    private ExecutionRequest generateRequestWithAction(User user, Action action) {
        ExecutionRequest request = new ExecutionRequest();
        String uuid = UUID.randomUUID().toString();
        request.setId(uuid);
        request.setAction(action);
        request.setRequestor(user.getEmail());
        return request;
    }

    private String getExecuteUrl(String publicIpAddress) {
        return "http://" + publicIpAddress + ":" + minecraftServerPort + "/minecraft/execute";
    }
}
