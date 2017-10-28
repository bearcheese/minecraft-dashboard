package hu.bearmaster.minecraftstarter.dashboard.domain.response;

public class ExecutionResponse {

    private String id;

    private ResponseStatus status;

    private String message;
    
    private MinecraftDetails minecraftDetails;

    public ExecutionResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MinecraftDetails getMinecraftDetails() {
        return minecraftDetails;
    }

    public void setMinecraftDetails(MinecraftDetails minecraftDetails) {
        this.minecraftDetails = minecraftDetails;
    }

    @Override
    public String toString() {
        return "ExecutionResponse [id=" + id + ", status=" + status + ", message=" + message + ", minecraftDetails=" + minecraftDetails + "]";
    }

}
