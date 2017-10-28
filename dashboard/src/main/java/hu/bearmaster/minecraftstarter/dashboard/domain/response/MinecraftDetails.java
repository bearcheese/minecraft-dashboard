package hu.bearmaster.minecraftstarter.dashboard.domain.response;

import java.io.Serializable;

public class MinecraftDetails implements Serializable {
    
    private static final long serialVersionUID = -1109260962739907131L;

    private MapDetails mapDetails = new MapDetails();
    
    private ServerDetails serverDetails = new ServerDetails();

    public MapDetails getMapDetails() {
        return mapDetails;
    }

    public void setMapDetails(MapDetails mapDetails) {
        this.mapDetails = mapDetails;
    }

    public ServerDetails getServerDetails() {
        return serverDetails;
    }

    public void setServerDetails(ServerDetails serverDetails) {
        this.serverDetails = serverDetails;
    }

    @Override
    public String toString() {
        return "MinecraftDetails [mapDetails=" + mapDetails + ", serverDetails=" + serverDetails + "]";
    }

}
