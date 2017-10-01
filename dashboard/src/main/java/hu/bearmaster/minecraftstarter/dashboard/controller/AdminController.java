package hu.bearmaster.minecraftstarter.dashboard.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import hu.bearmaster.minecraftstarter.dashboard.domain.Ec2Instance;
import hu.bearmaster.minecraftstarter.dashboard.domain.User;
import hu.bearmaster.minecraftstarter.dashboard.service.AwsService;
import hu.bearmaster.minecraftstarter.dashboard.service.JwtGeneratorService;
import hu.bearmaster.minecraftstarter.dashboard.service.MinecraftServerService;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);


    @Value("${aws.asg.name}")
    private String autoScalingGroupName;

    private final AwsService awsService;

    private final JwtGeneratorService jwtGeneratorService;

    private final RestTemplate restTemplate;

    private final MinecraftServerService minecraftServerService;

    public AdminController(AwsService awsService, JwtGeneratorService jwtGeneratorService, RestTemplate restTemplate,
            MinecraftServerService minecraftServerService) {
        this.awsService = awsService;
        this.jwtGeneratorService = jwtGeneratorService;
        this.restTemplate = restTemplate;
        this.minecraftServerService = minecraftServerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String adminPage(@AuthenticationPrincipal User user, Model model) {
        LOGGER.info("Admin page loaded by {}", user);
        List<Ec2Instance> instances = minecraftServerService.getServerList(user);

        model.addAttribute("user", user);
        model.addAttribute("instances", instances);
        model.addAttribute("numberOfRunningInstances", countRunningInstances(instances));
        return "admin";
    }

    private long countRunningInstances(List<Ec2Instance> instances) {
        return instances.stream()
                .filter(instance -> instance.getState().equals("running"))
                .count();
    }

    //TODO move to MinecraftServerService
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public @ResponseBody String startServer() {
        LOGGER.info("Starting instance in {} group", autoScalingGroupName);
        awsService.upscaleAutoScalingGroup(autoScalingGroupName);
        return "OK";
    }

    //TODO move to MinecraftServerService + additional checks
    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public @ResponseBody String stopServer() {
        LOGGER.info("Stopping instance in {} group", autoScalingGroupName);
        awsService.downscaleAutoScalingGroup(autoScalingGroupName);
        return "OK";
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public @ResponseBody String majom(@AuthenticationPrincipal User user) {
        LOGGER.info("Server status invoked");

        LOGGER.info("");
        return "OK";
    }
}
