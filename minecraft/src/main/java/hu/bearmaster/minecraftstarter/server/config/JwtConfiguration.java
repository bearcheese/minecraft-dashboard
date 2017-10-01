package hu.bearmaster.minecraftstarter.server.config;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.auth0.jwt.algorithms.Algorithm;

@Configuration
public class JwtConfiguration {

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

}
