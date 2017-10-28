package hu.bearmaster.minecraftstarter.dashboard.domain.formatter;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;

import hu.bearmaster.minecraftstarter.dashboard.domain.response.MapName;

public class MapNameFormatter implements Formatter<MapName> {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");;

    @Override
    public String print(MapName mapName, Locale locale) {
        return mapName.getPrefix() + "-" + formatter.format(mapName.getDate()) + "." + mapName.getExtension();
    }

    @Override
    public MapName parse(String stringValue, Locale locale) throws ParseException {
        throw new UnsupportedOperationException("Map parsing not supported");
    }

}
