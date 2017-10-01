package hu.bearmaster.minecraftstarter.dashboard.init;

import static hu.bearmaster.minecraftstarter.dashboard.domain.Role.ADMIN;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import hu.bearmaster.minecraftstarter.dashboard.data.UserRepository;
import hu.bearmaster.minecraftstarter.dashboard.domain.User;

@Component
public class UserInitializer implements CommandLineRunner {

    private UserRepository userRepository;

    public UserInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void run(String... arg0) throws Exception {
        initUserRepository();
    }

    private void initUserRepository() {
        userRepository.save(new User("bearcheese" + String.valueOf(Character.toChars(64)) + "gmail.com", ADMIN));
    }

}