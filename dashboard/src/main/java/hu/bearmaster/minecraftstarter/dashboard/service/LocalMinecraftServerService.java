package hu.bearmaster.minecraftstarter.dashboard.service;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import hu.bearmaster.minecraftstarter.dashboard.domain.ServerInstance;
import hu.bearmaster.minecraftstarter.dashboard.domain.User;

@Service
@Profile("default")
public class LocalMinecraftServerService extends AbstractMinecraftServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMinecraftServerService.class);

    private Optional<ServerInstance> server = Optional.empty();

    protected LocalMinecraftServerService(RequestGeneratorService requestGeneratorService, RestTemplate restTemplate) {
        super(requestGeneratorService, restTemplate);
    }

    @Override
    public Optional<ServerInstance> getActiveServer(User user) {
        server.ifPresent(instance -> enrichInstancesWithMinecraftServerInfo(user, instance));
        return server;
    }

    @Override
    public void startUpServerInstance() {
        ServerInstance localInstance = new ServerInstance();
        localInstance.setInstanceId("local");
        localInstance.setPublicIpAddress("127.0.0.1");
        localInstance.setLaunchTime(Instant.now());
        localInstance.setState("running");
        server = Optional.of(localInstance);
    }

    @Override
    public void stopServerInstance() {
        server = Optional.empty();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
