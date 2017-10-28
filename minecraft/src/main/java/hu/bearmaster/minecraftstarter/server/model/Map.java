package hu.bearmaster.minecraftstarter.server.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Map implements Serializable {

    private static final long serialVersionUID = 445984561386928825L;

    private MapName name;
    
    private LocalDateTime time;

    public Map(MapName name, LocalDateTime time) {
        super();
        this.name = name;
        this.time = time;
    }

    public MapName getName() {
        return name;
    }

    public void setName(MapName name) {
        this.name = name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Map [name=" + name + ", time=" + time + "]";
    }
    
}
