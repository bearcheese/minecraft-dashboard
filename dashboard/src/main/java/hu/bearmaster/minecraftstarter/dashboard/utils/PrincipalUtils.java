package hu.bearmaster.minecraftstarter.dashboard.utils;

import java.util.Optional;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

import hu.bearmaster.minecraftstarter.dashboard.domain.User;

public final class PrincipalUtils {

    public static Optional<User> extractUser(OAuth2Authentication principal) {
        return Optional.ofNullable(principal)
                .filter(p -> p.getUserAuthentication().getPrincipal() instanceof User)
                .map(p -> (User)p.getUserAuthentication().getPrincipal());
    }

    private PrincipalUtils() {
    }
}
