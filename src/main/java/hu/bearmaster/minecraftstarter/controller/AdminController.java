package hu.bearmaster.minecraftstarter.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hu.bearmaster.minecraftstarter.domain.Ec2Instance;
import hu.bearmaster.minecraftstarter.domain.User;
import hu.bearmaster.minecraftstarter.service.AwsService;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin")
@Profile("frontend")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
    private static final String AUTO_SCALING_GROUP_NAME = "murmur-server";

    private final AwsService awsService;

    public AdminController(AwsService awsService) {
        this.awsService = awsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String adminPage(@AuthenticationPrincipal User user, Model model) {
        List<String> instanceIds = awsService.getInstancesIdsOfAutoScalingGroup(AUTO_SCALING_GROUP_NAME);
        List<Ec2Instance> instances = awsService.getInstancesByIds(instanceIds);
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

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public @ResponseBody String startServer() {
        LOGGER.info("Starting instance in {} group", AUTO_SCALING_GROUP_NAME);
        awsService.upscaleAutoScalingGroup(AUTO_SCALING_GROUP_NAME);
        return "OK";
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public @ResponseBody String stopServer() {
        LOGGER.info("Stopping instance in {} group", AUTO_SCALING_GROUP_NAME);
        awsService.downscaleAutoScalingGroup(AUTO_SCALING_GROUP_NAME);
        return "OK";
    }
}
