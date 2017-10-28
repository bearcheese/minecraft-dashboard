package hu.bearmaster.minecraftstarter.dashboard.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bearmaster.minecraftstarter.dashboard.domain.ExecutionRequest;

@Service
@Profile("default")
public class ClearTextRequestService implements RequestGeneratorService {

    private final ObjectMapper objectMapper;

    public ClearTextRequestService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateSignedRequest(ExecutionRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot generate request from " + request);
        }
    }
}
