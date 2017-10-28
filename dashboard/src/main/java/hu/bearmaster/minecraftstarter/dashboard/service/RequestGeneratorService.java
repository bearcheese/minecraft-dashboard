package hu.bearmaster.minecraftstarter.dashboard.service;

import hu.bearmaster.minecraftstarter.dashboard.domain.ExecutionRequest;

public interface RequestGeneratorService {
    String generateSignedRequest(ExecutionRequest request);
}
