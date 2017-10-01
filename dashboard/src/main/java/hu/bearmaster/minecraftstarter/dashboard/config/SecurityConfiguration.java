package hu.bearmaster.minecraftstarter.dashboard.config;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.algorithms.Algorithm;
import hu.bearmaster.minecraftstarter.dashboard.data.UserRepository;
import hu.bearmaster.minecraftstarter.dashboard.domain.User;

@EnableWebSecurity
@EnableOAuth2Sso
@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${keystore.password}")
    public String keyStorePassword;

    @Value("${keystore.key.password}")
    public String keyStoreKeyPassword;

    @Value("${keystore.key.alias}")
    public String keyAlias;

    @Value("${keystore.file}")
    public Resource keyStoreFile;

    @Bean
    public Algorithm jwtAlgorithm() throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                keyStoreFile, keyStorePassword.toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(keyAlias, keyStoreKeyPassword.toCharArray());

        return Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateCrtKey) keyPair.getPrivate());

    }

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

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
