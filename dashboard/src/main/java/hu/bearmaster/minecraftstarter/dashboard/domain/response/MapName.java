package hu.bearmaster.minecraftstarter.dashboard.domain.response;

import java.io.Serializable;
import java.time.LocalDate;

public class MapName implements Comparable<MapName>, Serializable {

    private static final long serialVersionUID = -3571752515547843862L;

    private String prefix;

    private LocalDate date;

    private String extension;
    
    private MapName() {
    }

    private MapName(String prefix, LocalDate date, String extension) {
        this.prefix = prefix;
        this.date = date;
        this.extension = extension;
    }

    public String getPrefix() {
        return prefix;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MapName mapName = (MapName) o;

        if (prefix != null ? !prefix.equals(mapName.prefix) : mapName.prefix != null)
            return false;
        if (date != null ? !date.equals(mapName.date) : mapName.date != null)
            return false;
        return extension != null ? extension.equals(mapName.extension) : mapName.extension == null;
    }

    @Override
    public int hashCode() {
        int result = prefix != null ? prefix.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        return result;
    }

    @Override 
    public String toString() {
        return "MapName{" +
                "prefix='" + prefix + '\'' +
                ", date=" + date +
                ", extension='" + extension + '\'' +
                '}';
    }

    @Override
    public int compareTo(MapName other) {
        return this.date.compareTo(other.date);
    }

    public static MapName mapName(String prefix, LocalDate date, String extension) {
        return new MapName(prefix, date, extension);
    }
}
