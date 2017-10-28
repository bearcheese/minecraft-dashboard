package hu.bearmaster.minecraftstarter.dashboard.domain;

import java.time.Instant;

import hu.bearmaster.minecraftstarter.dashboard.domain.response.MinecraftDetails;
import software.amazon.awssdk.services.ec2.model.Instance;

public class ServerInstance {

    private String instanceId;

    private String publicIpAddress;

    private String state;

    private Instant launchTime;

    private MinecraftDetails minecraftDetails;

    public ServerInstance() {
    }

    public ServerInstance(Instance instance) {
        this.instanceId = instance.instanceId();
        this.publicIpAddress = instance.publicIpAddress();
        this.state = instance.state().name();
        this.launchTime = instance.launchTime();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getPublicIpAddress() {
        return publicIpAddress;
    }

    public void setPublicIpAddress(String publicIpAddress) {
        this.publicIpAddress = publicIpAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Instant getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Instant launchTime) {
        this.launchTime = launchTime;
    }

    public MinecraftDetails getMinecraftDetails() {
        return minecraftDetails;
    }

    public void setMinecraftDetails(MinecraftDetails minecraftServerInfo) {
        this.minecraftDetails = minecraftServerInfo;
    }
}
