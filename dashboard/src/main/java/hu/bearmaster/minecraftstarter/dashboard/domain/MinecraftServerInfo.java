package hu.bearmaster.minecraftstarter.dashboard.domain;

import java.util.Map;

public class MinecraftServerInfo {

    private String status;

    private String title;

    private String version;

    private int players;

    private int maxPlayers;

    public MinecraftServerInfo() {
    }

    public MinecraftServerInfo(Map<String, Object> additionalInfo) {
        this.status = (String)additionalInfo.get("server_status");
        if (this.status.equals("running")) {
            this.title = ((Map<String, Object>) additionalInfo.get("description")).get("text").toString();
            this.version = ((Map<String, Object>) additionalInfo.get("version")).get("name").toString();
            this.players = Integer.valueOf(((Map<String, Object>) additionalInfo.get("players")).get("online").toString());
            this.players = Integer.valueOf(((Map<String, Object>) additionalInfo.get("players")).get("max").toString());
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
