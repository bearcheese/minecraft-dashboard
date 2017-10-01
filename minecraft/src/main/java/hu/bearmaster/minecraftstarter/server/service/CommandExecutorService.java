package hu.bearmaster.minecraftstarter.server.service;

import static hu.bearmaster.minecraftstarter.server.model.Status.FAILED;
import static hu.bearmaster.minecraftstarter.server.model.Status.SUCCESSFUL;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import hu.bearmaster.minecraftstarter.server.command.Command;
import hu.bearmaster.minecraftstarter.server.model.CommandDetails;
import hu.bearmaster.minecraftstarter.server.model.ExecutionResponse;

@Service
public class CommandExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutorService.class);

    private final List<Command> commands;

    public CommandExecutorService(List<Command> commands) {
        this.commands = commands;
    }

    public ExecutionResponse execute(CommandDetails commandDetails) {
        LOGGER.info("CommandDetails received: {}", commandDetails);
        ExecutionResponse response = null;
        for (Command command : commands) {
            if (command.isExecutedOn(commandDetails.getAction())) {
                LOGGER.info("Executing {} with {}", commandDetails.getAction().name(), command.getClass().getSimpleName());
                response = command.execute(commandDetails);
                break;
            }
        }
        if (response == null) {
            response = new ExecutionResponse();
            response.setStatus(FAILED);
            response.addAdditionalInfo("message", "Command not found");
        }
        response.setId(commandDetails.getId());
        LOGGER.info("CommandDetails executed with results: {}", response);
        return response;
    }
}
