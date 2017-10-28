package hu.bearmaster.minecraftstarter.dashboard.service;

import java.util.Optional;

import hu.bearmaster.minecraftstarter.dashboard.domain.ServerInstance;
import hu.bearmaster.minecraftstarter.dashboard.domain.User;
import hu.bearmaster.minecraftstarter.dashboard.domain.response.ExecutionResponse;

public interface MinecraftServerService {
    Optional<ServerInstance> getActiveServer(User user);

    void startUpServerInstance();

    void stopServerInstance();

    ExecutionResponse loadLatestMap(User user);

    ExecutionResponse saveCurrentMap(User user);

    ExecutionResponse startMinecraftServer(User user);

    ExecutionResponse stopMinecraftServer(User user);

    Optional<ServerInstance> getServerStatus(User user);
}
