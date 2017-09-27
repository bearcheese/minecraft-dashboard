package hu.bearmaster.minecraftstarter.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/minecraft")
@Profile("backend")
public class MinecraftController {

    //start minecraft server
    //stop minecraft server
    //load map from S3
    //save map to S3
    //server health check
    //log retrieval

}
