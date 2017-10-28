package hu.bearmaster.minecraftstarter.server.service.decoder;

import java.io.IOException;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bearmaster.minecraftstarter.server.model.CommandDetails;

@Service
@Profile("default")
public class ClearTextCommandDecoderService implements CommandDecoderService {

    private final ObjectMapper objectMapper;

    public ClearTextCommandDecoderService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public CommandDetails decode(String token) {
        try {
            return objectMapper.readerFor(CommandDetails.class).readValue(token);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot process request: " + token);
        }
    }
}
