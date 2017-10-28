package hu.bearmaster.minecraftstarter.server.model;

import static hu.bearmaster.minecraftstarter.server.model.ServerStatus.STOPPED;

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.Nullable;

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

    public Optional<ServerInfo> getInfo() {
        return Optional.ofNullable(info);
    }

    public void setInfo(@Nullable ServerInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ServerDetails [status=" + status + ", info=" + getInfo().map(ServerInfo::toString).orElse("n/a") + "]";
    }

}
