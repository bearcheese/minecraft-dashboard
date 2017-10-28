package hu.bearmaster.minecraftstarter.dashboard.domain.response;

import java.io.Serializable;

public class MapDetails implements Serializable {
    
    private static final long serialVersionUID = 390926177386977523L;

    private Map loadedMap;
    
    private Map savedMap;

    public Map getLoadedMap() {
        return loadedMap;
    }

    public void setLoadedMap(Map loadedMap) {
        this.loadedMap = loadedMap;
    }

    public Map getSavedMap() {
        return savedMap;
    }

    public void setSavedMap(Map savedMap) {
        this.savedMap = savedMap;
    }

    @Override
    public String toString() {
        return "MapDetails [loadedMap=" + loadedMap + ", savedMap=" + savedMap + "]";
    }

}
