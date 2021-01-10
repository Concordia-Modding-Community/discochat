package ca.concordia.mccord;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.concordia.mccord.commands.CommandManager;
import ca.concordia.mccord.data.DataManager;
import ca.concordia.mccord.discord.DiscordManager;
import ca.concordia.mccord.events.EventManager;
import ca.concordia.mccord.server.ServerManager;

@Mod(Resources.MOD_ID)
public class MCCord {
    public static final Logger LOGGER = LogManager.getLogger();

    public MCCord() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        EventManager.register(MinecraftForge.EVENT_BUS);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onServerStopped);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onCommandRegister);
    }

    private void onCommandRegister(final RegisterCommandsEvent event) {
        CommandManager.register(event.getDispatcher());
    }

    private void onServerStarted(final FMLServerStartedEvent event) {
        ServerManager.register(event.getServer());
        DiscordManager.register();
        DataManager.register();
    }

    private void onServerStopped(final FMLServerStoppedEvent event) {
        DataManager.unregister();
        DiscordManager.unregister();
        ServerManager.unregister();
    }
}