package hu.bearmaster.minecraftstarter.dashboard.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hu.bearmaster.minecraftstarter.dashboard.domain.ServerInstance;
import hu.bearmaster.minecraftstarter.dashboard.domain.User;
import hu.bearmaster.minecraftstarter.dashboard.domain.response.ExecutionResponse;
import hu.bearmaster.minecraftstarter.dashboard.service.MinecraftServerService;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    private final MinecraftServerService minecraftServerService;

    public AdminController(MinecraftServerService minecraftServerService) {
        this.minecraftServerService = minecraftServerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String adminPage(@AuthenticationPrincipal User user, Model model) {
        LOGGER.info("Admin page loaded by {}", user);
        Optional<ServerInstance> instance = minecraftServerService.getActiveServer(user);

        model.addAttribute("user", user);
        model.addAttribute("instance", instance.orElse(null));
        return "admin";
    }

    @RequestMapping(value = "/start-instance", method = RequestMethod.POST)
    public @ResponseBody String startInstance() {
        minecraftServerService.startUpServerInstance();
        return "OK";
    }

    @RequestMapping(value = "/stop-instance", method = RequestMethod.POST)
    public @ResponseBody String stopInstance() {
        minecraftServerService.stopServerInstance();
        return "OK";
    }

    @RequestMapping(value = "/load-map", method = RequestMethod.POST)
    public @ResponseBody ExecutionResponse loadMap(@AuthenticationPrincipal User user) {
        return minecraftServerService.loadLatestMap(user);
    }

    @RequestMapping(value = "/save-map", method = RequestMethod.POST)
    public @ResponseBody ExecutionResponse saveMap(@AuthenticationPrincipal User user) {
        return minecraftServerService.saveCurrentMap(user);
    }

    @RequestMapping(value = "/start-server", method = RequestMethod.POST)
    public @ResponseBody ExecutionResponse startServer(@AuthenticationPrincipal User user) {
        return minecraftServerService.startMinecraftServer(user);
    }

    @RequestMapping(value = "/stop-server", method = RequestMethod.POST)
    public @ResponseBody ExecutionResponse stopServer(@AuthenticationPrincipal User user) {
        return minecraftServerService.stopMinecraftServer(user);
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public @ResponseBody ServerInstance serverStatus(@AuthenticationPrincipal User user) {
        return minecraftServerService.getServerStatus(user).orElse(null);
    }
}
