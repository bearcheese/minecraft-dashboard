package hu.bearmaster.minecraftstarter.dashboard.domain.response;

import static hu.bearmaster.minecraftstarter.dashboard.domain.response.ServerStatus.STOPPED;

import java.io.Serializable;

public class ServerDetails implements Serializable {
    
    private static final long serialVersionUID = 852624382796125259L;

    private ServerStatus status = STOPPED;
    
    private ServerInfo info;

    public ServerStatus getStatus() {
        return status;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }

    public ServerInfo getInfo() {
        return info;
    }

    public void setInfo(ServerInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ServerDetails [status=" + status + ", info=" + info + "]";
    }

}
