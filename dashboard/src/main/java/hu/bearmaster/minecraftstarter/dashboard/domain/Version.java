package hu.bearmaster.minecraftstarter.dashboard.domain;

public class Version {

    private String name;

    private int protocol;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }
}
