package hu.bearmaster.minecraftstarter.server.service.decoder;

import hu.bearmaster.minecraftstarter.server.model.CommandDetails;

public interface CommandDecoderService {
    CommandDetails decode(String token);
}
