package hu.bearmaster.minecraftstarter.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import hu.bearmaster.minecraftstarter.data.UserRepository;
import hu.bearmaster.minecraftstarter.domain.User;

@EnableWebSecurity
@EnableOAuth2Sso
@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserRepository userRepository) {
        return map -> {
            String email = (String) map.get("email");
            Optional<User> user = userRepository.findByEmail(email);
            user.ifPresent(u -> u.setName((String)map.getOrDefault("name", "<unknown user>")));
            return user.orElse(null);
        };
    }

    @Bean
    public AuthoritiesExtractor authoritiesExtractor(UserRepository userRepository) {
        return map -> {
            String email = (String) map.get("email");
            Optional<User> user = userRepository.findByEmail(email);
            List<GrantedAuthority> grants = new ArrayList<>();
            user.ifPresent(u -> grants.add(new SimpleGrantedAuthority(u.getRole().toString())));
            return grants;
        };
    }
}
