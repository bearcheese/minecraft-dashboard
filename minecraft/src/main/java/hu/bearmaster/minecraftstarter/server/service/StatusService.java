package hu.bearmaster.minecraftstarter.server.service;

import static hu.bearmaster.minecraftstarter.server.model.ServerStatus.RUNNING;
import static hu.bearmaster.minecraftstarter.server.model.ServerStatus.STOPPED;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.bearmaster.minecraftstarter.server.model.Map;
import hu.bearmaster.minecraftstarter.server.model.MapName;
import hu.bearmaster.minecraftstarter.server.model.MinecraftDetails;
import hu.bearmaster.minecraftstarter.server.model.ServerInfo;
import hu.bearmaster.minecraftstarter.server.model.internal.MinecraftServerInfo;
import hu.bearmaster.minecraftstarter.server.util.MinecraftServerListPing;

@Service
public class StatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);

    private final MinecraftServerListPing ping;

    private ObjectMapper mapper;

    private MinecraftDetails minecraftDetails;

    public StatusService(ObjectMapper mapper) {
        this.mapper = mapper;
        this.ping = new MinecraftServerListPing(new InetSocketAddress("localhost", 25565));
        this.minecraftDetails = new MinecraftDetails();
    }

    public synchronized MinecraftDetails registerMapLoaded(MapName loadedMap) {
        LOGGER.info("Registering map '{}' loaded", loadedMap);
        minecraftDetails.getMapDetails().setLoadedMap(new Map(loadedMap, LocalDateTime.now()));
        minecraftDetails.getMapDetails().setSavedMap(null);
        return SerializationUtils.clone(minecraftDetails);
    }

    public synchronized MinecraftDetails registerMapSaved(MapName savedMap) {
        LOGGER.info("Registering map '{}' saved", savedMap);
        minecraftDetails.getMapDetails().setSavedMap(new Map(savedMap, LocalDateTime.now()));
        return SerializationUtils.clone(minecraftDetails);
    }

    public synchronized MinecraftDetails registerServerStarted() {
        minecraftDetails.getServerDetails().setStatus(RUNNING);
        return getCurrentStatus();
    }

    public synchronized MinecraftDetails registerServerStopped() {
        minecraftDetails.getServerDetails().setStatus(STOPPED);
        return getCurrentStatus();
    }

    private void updateServerState() {
        try {
            String statusResponse = ping.fetchData();
            MinecraftServerInfo internalState = mapper.readValue(statusResponse, MinecraftServerInfo.class);
            minecraftDetails.getServerDetails().setStatus(RUNNING);
            minecraftDetails.getServerDetails().setInfo(new ServerInfo(internalState));
        } catch (IOException ioe) {
            LOGGER.warn("Cannot fetch server data, probably it's not running");
            minecraftDetails.getServerDetails().setStatus(STOPPED);
            minecraftDetails.getServerDetails().setInfo(null);
        }
    }

    public synchronized MinecraftDetails getCurrentStatus() {
        updateServerState();
        return SerializationUtils.clone(minecraftDetails);
    }
}
