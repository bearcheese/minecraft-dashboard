package hu.bearmaster.minecraftstarter.dashboard.service;

import java.time.Instant;
import java.util.Date;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import hu.bearmaster.minecraftstarter.dashboard.domain.ExecutionRequest;

@Service
@Profile("production")
public class JwtGeneratorService implements RequestGeneratorService {

    private final Algorithm algorithm;

    public JwtGeneratorService(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override public String generateSignedRequest(ExecutionRequest request) {

        return JWT.create()
                .withJWTId(request.getId())
                .withIssuer("dashboard")
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(2)))
                .withSubject(request.getAction().subject)
                .withClaim("requestor", request.getRequestor())
                .sign(algorithm);
    }
}
