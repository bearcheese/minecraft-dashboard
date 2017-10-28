package hu.bearmaster.minecraftstarter.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.bearmaster.minecraftstarter.server.model.CommandDetails;
import hu.bearmaster.minecraftstarter.server.model.ExecutionResponse;
import hu.bearmaster.minecraftstarter.server.service.CommandExecutorService;
import hu.bearmaster.minecraftstarter.server.service.decoder.CommandDecoderService;

@RestController
@RequestMapping("/minecraft")
public class MinecraftController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftController.class);

    private final CommandDecoderService commandDecoderService;

    private final CommandExecutorService commandExecutorService;

    public MinecraftController(CommandDecoderService commandDecoderService, CommandExecutorService commandExecutorService) {
        this.commandDecoderService = commandDecoderService;
        this.commandExecutorService = commandExecutorService;
    }

    @PostMapping("/execute")
    public ExecutionResponse executeCommand(@RequestBody String jwtToken) {
        LOGGER.info("Minecraft execute command invoked");
        CommandDetails commandDetails = commandDecoderService.decode(jwtToken);
        return commandExecutorService.execute(commandDetails);
    }

}
