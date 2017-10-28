package hu.bearmaster.minecraftstarter.dashboard.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import hu.bearmaster.minecraftstarter.dashboard.domain.ServerInstance;
import hu.bearmaster.minecraftstarter.dashboard.domain.User;

@Service
@Profile("production")
public class AwsMinecraftServerService extends AbstractMinecraftServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsMinecraftServerService.class);

    @Value("${aws.asg.name}")
    private String autoScalingGroupName;

    private final AwsService awsService;

    public AwsMinecraftServerService(AwsService awsService, RequestGeneratorService requestGeneratorService, RestTemplate restTemplate) {
        super(requestGeneratorService, restTemplate);
        this.awsService = awsService;
    }

    @Override
    public Optional<ServerInstance> getActiveServer(User user) {
        List<String> instanceIds = awsService.getInstancesIdsOfAutoScalingGroup(autoScalingGroupName);
        List<ServerInstance> instances = awsService.getInstancesByIds(instanceIds);
        Optional<ServerInstance> runningInstance = instances.stream().filter(instance -> instance.getState().equals("running")).findFirst();
        runningInstance.ifPresent(instance -> enrichInstancesWithMinecraftServerInfo(user, instance));
        return runningInstance;
    }

    @Override
    public void startUpServerInstance() {
        LOGGER.info("Starting instance in {} group", autoScalingGroupName);
        awsService.upscaleAutoScalingGroup(autoScalingGroupName);
    }

    @Override
    public void stopServerInstance() {
        //TODO add failsafe checks for saving the world map
        LOGGER.info("Stopping instance in {} group", autoScalingGroupName);
        awsService.downscaleAutoScalingGroup(autoScalingGroupName);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

}
