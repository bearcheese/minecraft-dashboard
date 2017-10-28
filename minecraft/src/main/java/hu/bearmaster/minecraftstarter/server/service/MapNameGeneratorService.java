package hu.bearmaster.minecraftstarter.server.service;

import javax.annotation.PostConstruct;

import static hu.bearmaster.minecraftstarter.server.model.MapName.mapName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hu.bearmaster.minecraftstarter.server.model.MapName;

@Service
public class MapNameGeneratorService {

    private static final String GROUP_SEPARATOR = "-";
    private static final String EXTENSION_SEPARATOR = ".";

    private DateTimeFormatter formatter;

    @Value("${map.name.prefix}")
    private String prefix;

    @Value("${map.name.extension}")
    private String extension;

    @Value("${map.name.date-format}")
    private String dateFormat;

    @PostConstruct
    public void setFormatter() {
        formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    public Optional<MapName> parse(String text) {
        if (text.startsWith(prefix + GROUP_SEPARATOR) && text.endsWith(EXTENSION_SEPARATOR + extension)) {
            String datePart = text.substring(prefix.length() + GROUP_SEPARATOR.length(), text.length() - EXTENSION_SEPARATOR.length() - extension.length());
            try {
                LocalDate date = LocalDate.parse(datePart, formatter);
                return Optional.of(mapName(prefix, date, extension));
            } catch (DateTimeParseException e) {
                //do nothing, will return empty
            }
        }
        return Optional.empty();
    }

    public String format(MapName mapName) {
        return mapName.getPrefix() + GROUP_SEPARATOR + formatter.format(mapName.getDate()) + EXTENSION_SEPARATOR + mapName.getExtension();
    }

    public MapName generate(LocalDate localDate) {
        return mapName(prefix, localDate, extension);
    }
}
