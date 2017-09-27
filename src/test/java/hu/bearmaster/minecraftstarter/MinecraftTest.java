package hu.bearmaster.minecraftstarter;

import java.io.IOException;
import java.net.InetSocketAddress;

import hu.bearmaster.minecraftstarter.utils.MinecraftServerListPing;

public class MinecraftTest {

    public static void main(String[] args) throws IOException {
        MinecraftServerListPing ping = new MinecraftServerListPing();

        ping.setAddress(new InetSocketAddress("localhost", 25565));
        String statusResponse = ping.fetchData();
        System.out.println(statusResponse);
    }
}
