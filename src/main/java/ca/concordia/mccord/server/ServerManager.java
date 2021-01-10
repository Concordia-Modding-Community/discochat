package ca.concordia.mccord.server;

import java.util.Optional;

import net.minecraft.server.MinecraftServer;

public class ServerManager {
    private static MinecraftServer SERVER = null;

    public static void register(MinecraftServer server) {
        SERVER = server;
    }

    public static void unregister() {
        SERVER = null;
    }

    public static Optional<MinecraftServer> getServer() {
        return Optional.ofNullable(SERVER);
    }
}
