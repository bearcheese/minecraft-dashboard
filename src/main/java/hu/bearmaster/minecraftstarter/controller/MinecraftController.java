package hu.bearmaster.minecraftstarter.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/minecraft")
@Profile("backend")
public class MinecraftController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinecraftController.class);

    @Value("${minecraft.root.path}")
    private File rootDir;

    @Value("${minecraft.start.script}")
    private String startScriptName;

    //start minecraft server
    @PostMapping("/start")
    public String startMinecraftServer() {
        LOGGER.info("Minecraft started invoked");

        File startScript = new File(rootDir, startScriptName);
        StringBuffer sb = new StringBuffer();

        Process p;
        try {
            LOGGER.info("Command to be executed: {}", startScript.getAbsolutePath());
            p = Runtime.getRuntime().exec(startScript.getAbsolutePath());
            p.waitFor();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info("Minecraft starter finished");
        LOGGER.info("Command output: {}", sb.toString());
        return sb.toString();
    }

    //stop minecraft server
    //load map from S3
    //save map to S3
    //server health check
    //log retrieval

}
