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

    private final AwsService awsService;

    private final MinecraftServerService minecraftServerService;

    public AdminController(AwsService awsService, MinecraftServerService minecraftServerService) {
        this.awsService = awsService;
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

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public @ResponseBody String startServer() {
        minecraftServerService.startUpServerInstance();
        return "OK";
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public @ResponseBody String stopServer() {
        minecraftServerService.stopServerInstance();
        return "OK";
    }
}
