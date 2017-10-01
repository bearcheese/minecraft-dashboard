package hu.bearmaster.minecraftstarter.server.command;

import static hu.bearmaster.minecraftstarter.server.model.Action.STATUS;
import static hu.bearmaster.minecraftstarter.server.model.Status.SUCCESSFUL;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bearmaster.minecraftstarter.server.model.Action;
import hu.bearmaster.minecraftstarter.server.model.CommandDetails;
import hu.bearmaster.minecraftstarter.server.model.ExecutionResponse;
import hu.bearmaster.minecraftstarter.server.util.MinecraftServerListPing;

@Component
public class StatusCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusCommand.class);

    public static final String SERVER_STATUS = "server_status";
    private final MinecraftServerListPing ping;

    private final ObjectMapper mapper;

    public StatusCommand(ObjectMapper mapper) {
        this.mapper = mapper;
        this.ping = new MinecraftServerListPing();
        this.ping.setAddress(new InetSocketAddress("localhost", 25565));
    }

    @Override
    public ExecutionResponse execute(CommandDetails commandDetails) {
        ExecutionResponse response = new ExecutionResponse();
        try {
            String statusResponse = ping.fetchData();
            response.addAdditionalInfo(SERVER_STATUS, "running");
            response.getAdditionalInfo().putAll(mapper.readValue(statusResponse, new TypeReference<Map<String, Object>>(){}));
        } catch (IOException e) {
            LOGGER.info("Ping failed with: {}", e.getMessage());
            response.addAdditionalInfo(SERVER_STATUS, "stopped");
        }
        response.setStatus(SUCCESSFUL);
        return response;
    }

    @Override
    public boolean isExecutedOn(Action action) {
        return action == STATUS;
    }
}
