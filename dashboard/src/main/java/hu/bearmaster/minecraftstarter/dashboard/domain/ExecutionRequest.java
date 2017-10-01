package hu.bearmaster.minecraftstarter.dashboard.domain;

public class ExecutionRequest {

    private String id;

    private String subject;

    private String requestor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }
}
