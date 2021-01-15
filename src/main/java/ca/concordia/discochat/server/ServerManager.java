package ca.concordia.discochat.server;

import java.util.Optional;

import ca.concordia.discochat.utils.AbstractManager;
import ca.concordia.discochat.utils.IMod;
import net.minecraft.server.MinecraftServer;

public class ServerManager extends AbstractManager {
    private MinecraftServer server = null;

    public ServerManager(IMod mod) {
        super(mod);
    }

    public ServerManager register(MinecraftServer server) {
        this.server = server;

        return this;
    }

    public void unregister() {
        this.server = null;
    }

    public Optional<MinecraftServer> getServer() {
        return Optional.ofNullable(this.server);
    }

    public int getPlayerCount() {
        return server.getCurrentPlayerCount();
    }
}
