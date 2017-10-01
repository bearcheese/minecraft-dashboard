package hu.bearmaster.minecraftstarter.server.service;

import static hu.bearmaster.minecraftstarter.server.model.Action.action;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import hu.bearmaster.minecraftstarter.server.model.CommandDetails;

@Service
public class CommandDecoderService {

    private final Algorithm algorithm;

    public CommandDecoderService(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public CommandDetails decode(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("dashboard")
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return convertToCommand(decodedJWT);
    }

    private CommandDetails convertToCommand(DecodedJWT decodedJWT) {
        CommandDetails commandDetails = new CommandDetails();
        commandDetails.setId(decodedJWT.getId());
        commandDetails.setAction(action(decodedJWT.getSubject()));
        commandDetails.setRequestor(decodedJWT.getClaim("requestor").asString());
        return commandDetails;
    }

}
