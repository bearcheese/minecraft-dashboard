package hu.bearmaster.minecraftstarter.server.model;

import java.io.Serializable;

import hu.bearmaster.minecraftstarter.server.model.internal.MinecraftServerInfo;

public class ServerInfo implements Serializable {
    
    private static final long serialVersionUID = -8235822695637840332L;

    private String title;
    
    private Players players;
    
    private String version;
    
    public ServerInfo() {    
    }
    
    public ServerInfo(MinecraftServerInfo internalInfo) {
        this.title = internalInfo.getDescription().getText();
        this.players = internalInfo.getPlayers();
        this.version = internalInfo.getVersion().getName();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Players getPlayers() {
        return players;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ServerInfo [title=" + title + ", players=" + players + ", version=" + version + "]";
    }

}
