package hu.bearmaster.minecraftstarter.dashboard.domain;

public class ExecutionRequest {

    private String id;

    private Action action;

    private String requestor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action subject) {
        this.action = subject;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    @Override
    public String toString() {
        return "ExecutionRequest{" +
                "id='" + id + '\'' +
                ", action=" + action +
                ", requestor='" + requestor + '\'' +
                '}';
    }
}
