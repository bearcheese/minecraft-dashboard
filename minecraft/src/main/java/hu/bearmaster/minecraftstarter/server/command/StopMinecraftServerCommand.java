package hu.bearmaster.minecraftstarter.server.command;

import static hu.bearmaster.minecraftstarter.server.model.Action.STOP_SERVER;
import static hu.bearmaster.minecraftstarter.server.model.Status.FAILED;
import static hu.bearmaster.minecraftstarter.server.model.Status.SUCCESSFUL;

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

@Component
public class StopMinecraftServerCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopMinecraftServerCommand.class);

    @Value("${minecraft.root.path}")
    private File rootDir;

    @Value("${minecraft.stop.script}")
    private String stopScriptName;

    @Override
    public ExecutionResponse execute(CommandDetails commandDetails) {
        File stopScript = new File(rootDir, stopScriptName);
        ExecutionResponse executionResponse = new ExecutionResponse();
        StringBuffer sb = new StringBuffer();

        Process p;
        try {
            LOGGER.info("Command to be executed: {}", stopScript.getAbsolutePath());
            p = Runtime.getRuntime().exec(stopScript.getAbsolutePath());
            p.waitFor();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            executionResponse.setStatus(SUCCESSFUL);
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            sb.append(e.getMessage());
            executionResponse.setStatus(FAILED);
        }
        executionResponse.addAdditionalInfo("message", sb.toString());
        return executionResponse;
    }

    @Override
    public boolean isExecutedOn(Action action) {
        return STOP_SERVER == action;
    }
}
