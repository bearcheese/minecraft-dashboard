package hu.bearmaster.minecraftstarter.controller;

import static hu.bearmaster.minecraftstarter.utils.PrincipalUtils.extractUser;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import hu.bearmaster.minecraftstarter.domain.User;

@Controller
@Profile("frontend")
public class HomeController {

    @RequestMapping("/")
    public String homePage(OAuth2Authentication principal, Model model) {
        Optional<User> user = extractUser(principal);
        if (principal != null && principal.getUserAuthentication().isAuthenticated()) {
            if (user.isPresent()) {
                return "redirect:/admin";
            } else {
                model.addAttribute("unknownUser", true);
            }
        }
        return "index";
    }
}
