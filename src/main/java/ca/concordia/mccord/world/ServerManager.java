package ca.concordia.mccord.world;

import java.util.Optional;

import ca.concordia.mccord.Resources;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

@Mod.EventBusSubscriber(modid = Resources.MOD_ID, bus = Bus.FORGE)
public class ServerManager {
    private static MinecraftServer SERVER = null;

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent event) {
        SERVER = event.getServer();
    }

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppedEvent event) {
        SERVER = null;
    }

    public static Optional<MinecraftServer> getServer() {
        return Optional.of(SERVER);
    }
}
