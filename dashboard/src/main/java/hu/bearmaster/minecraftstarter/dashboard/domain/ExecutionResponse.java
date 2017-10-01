package hu.bearmaster.minecraftstarter.dashboard.domain;

import java.util.HashMap;
import java.util.Map;

public class ExecutionResponse {

    private String id;

    private Status status;

    private Map<String, Object> additionalInfo;

    public ExecutionResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void addAdditionalInfo(String key, Object value) {
        if(this.additionalInfo == null) {
            this.additionalInfo = new HashMap<>();
        }
        this.additionalInfo.put(key, value);
    }

    @Override
    public String toString() {
        return "ExecutionResponse{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", additionalInfo=" + additionalInfo +
                '}';
    }
}
