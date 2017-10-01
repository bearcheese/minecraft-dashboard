package hu.bearmaster.minecraftstarter.server.model;

import java.util.Map;

public class CommandDetails {

    private String id;

    private Action action;

    private String requestor;

    private Map<String, Object> parameters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    @Override public String toString() {
        return "CommandDetails{" +
                "id='" + id + '\'' +
                ", action=" + action +
                ", requestor='" + requestor + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
