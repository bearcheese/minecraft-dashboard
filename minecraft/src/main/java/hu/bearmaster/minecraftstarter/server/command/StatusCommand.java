package hu.bearmaster.minecraftstarter.server.command;

import static hu.bearmaster.minecraftstarter.server.model.Action.STATUS;
import static hu.bearmaster.minecraftstarter.server.model.ResponseStatus.SUCCESSFUL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import hu.bearmaster.minecraftstarter.server.model.Action;
import hu.bearmaster.minecraftstarter.server.model.CommandDetails;
import hu.bearmaster.minecraftstarter.server.model.ExecutionResponse;
import hu.bearmaster.minecraftstarter.server.model.MinecraftDetails;
import hu.bearmaster.minecraftstarter.server.service.StatusService;

@Component
public class StatusCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusCommand.class);

    private final StatusService statusService;

    public StatusCommand(StatusService statusService) {
        this.statusService = statusService;
    }

    @Override
    public ExecutionResponse execute(CommandDetails commandDetails) {
        ExecutionResponse response = new ExecutionResponse();
        LOGGER.info("Checking server status");
        MinecraftDetails minecraftDetails = statusService.getCurrentStatus();
        response.setMinecraftDetails(minecraftDetails);
        response.setStatus(SUCCESSFUL);
        LOGGER.info("Server status command completed with {}", response);
        return response;
    }

    @Override
    public boolean isExecutedOn(Action action) {
        return action == STATUS;
    }
}
