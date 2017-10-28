package hu.bearmaster.minecraftstarter.server.command;

import static hu.bearmaster.minecraftstarter.server.model.Action.START_SERVER;
import static hu.bearmaster.minecraftstarter.server.model.ServerStatus.STOPPED;
import static hu.bearmaster.minecraftstarter.server.model.ResponseStatus.FAILED;
import static hu.bearmaster.minecraftstarter.server.model.ResponseStatus.SUCCESSFUL;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import hu.bearmaster.minecraftstarter.server.model.Action;
import hu.bearmaster.minecraftstarter.server.model.CommandDetails;
import hu.bearmaster.minecraftstarter.server.model.ExecutionResponse;
import hu.bearmaster.minecraftstarter.server.model.MinecraftDetails;
import hu.bearmaster.minecraftstarter.server.service.StatusService;

@Component
public class StartMinecraftServerCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartMinecraftServerCommand.class);

    @Value("${minecraft.root.path}")
    private File rootDir;

    @Value("${minecraft.start.script}")
    private String startScriptName;
    
    private final StatusService statusService;

    public StartMinecraftServerCommand(StatusService statusService) {
        this.statusService = statusService;
    }

    @Override
    public ExecutionResponse execute(CommandDetails commandDetails) {
        MinecraftDetails currentStatus = statusService.getCurrentStatus();
        if (currentStatus.getServerDetails().getStatus() == STOPPED) {
            return attemptToStartServer();
        } else {
            ExecutionResponse executionResponse = new ExecutionResponse();
            executionResponse.setStatus(FAILED);
            executionResponse.setMessage("Server is already running");
            executionResponse.setMinecraftDetails(currentStatus);
            return executionResponse; 
        }
    }

    private ExecutionResponse attemptToStartServer() {
        File startScript = new File(rootDir, startScriptName);
        ExecutionResponse executionResponse = new ExecutionResponse();
        StringBuffer sb = new StringBuffer();
        Process p;
        try {
            LOGGER.info("Command to be executed: {}", startScript.getAbsolutePath());
            p = Runtime.getRuntime().exec(startScript.getAbsolutePath());
            p.waitFor();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            MinecraftDetails details = statusService.registerServerStarted();
            executionResponse.setStatus(SUCCESSFUL);
            executionResponse.setMinecraftDetails(details);
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            sb.append(e.getMessage());
            executionResponse.setStatus(FAILED);
        }
        executionResponse.setMessage(sb.toString());
        return executionResponse;
    }

    @Override
    public boolean isExecutedOn(Action action) {
        return START_SERVER == action;
    }
}
