package hu.bearmaster.minecraftstarter.server.model;

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.Nullable;

public class MapDetails implements Serializable {
    
    private static final long serialVersionUID = 390926177386977523L;

    private Map loadedMap;
    
    private Map savedMap;

    public Optional<Map> getLoadedMap() {
        return Optional.ofNullable(loadedMap);
    }

    public void setLoadedMap(@Nullable Map loadedMap) {
        this.loadedMap = loadedMap;
    }

    public Optional<Map> getSavedMap() {
        return Optional.ofNullable(savedMap);
    }

    public void setSavedMap(@Nullable Map savedMap) {
        this.savedMap = savedMap;
    }

    @Override
    public String toString() {
        return "MapDetails [loadedMap=" + getLoadedMap().map(Map::toString).orElse("{not loaded}") + ", savedMap=" + getSavedMap().map(Map::toString).orElse("{not saved}") + "]";
    }

}
