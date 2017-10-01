package hu.bearmaster.minecraftstarter.server.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Action {

    START_SERVER("start"),
    STOP_SERVER("stop"),
    LOAD_MAP("load"),
    SAVE_MAP("save"),
    GET_LOGS("logs"),
    STATUS("status");

    private String subject;

    private static final Map<String, Action> REVERSE_LOOKUP
            = Arrays.stream(Action.values())
                .collect(Collectors.toMap(a -> a.subject, Function.identity()));

    Action(String subject) {
        this.subject = subject;
    }

    public static Action action(String subject) {
        if (REVERSE_LOOKUP.containsKey(subject)) {
            return REVERSE_LOOKUP.get(subject);
        }
        throw new IllegalArgumentException("Subject " + subject + " cannot be mapped to any action!");
    }

}
