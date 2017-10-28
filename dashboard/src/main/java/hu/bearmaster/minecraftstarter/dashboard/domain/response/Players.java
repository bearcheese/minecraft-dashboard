package hu.bearmaster.minecraftstarter.dashboard.domain.response;

import java.io.Serializable;

public class Players implements Serializable {
    
    private static final long serialVersionUID = -6496073815903292234L;

    private int online;
    
    private int max;

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Players [online=" + online + ", max=" + max + "]";
    }

}
