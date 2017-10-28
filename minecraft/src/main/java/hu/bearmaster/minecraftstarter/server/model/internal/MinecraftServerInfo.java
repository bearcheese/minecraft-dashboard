package hu.bearmaster.minecraftstarter.server.model.internal;

import hu.bearmaster.minecraftstarter.server.model.Players;

public class MinecraftServerInfo {
    
    private Description description;
    
    private Players players;
    
    private Version version;

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Players getPlayers() {
        return players;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "MinecraftServerInfo [description=" + description + ", players=" + players + ", version=" + version + "]";
    }

}
