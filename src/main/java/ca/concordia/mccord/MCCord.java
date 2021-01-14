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

import ca.concordia.mccord.chat.ChatManager;
import ca.concordia.mccord.commands.CommandManager;
import ca.concordia.mccord.commands.CommandSuggestions;
import ca.concordia.mccord.data.DataManager;
import ca.concordia.mccord.discord.DiscordManager;
import ca.concordia.mccord.entity.UserManager;
import ca.concordia.mccord.events.EventManager;
import ca.concordia.mccord.server.ServerManager;
import ca.concordia.mccord.utils.IMod;

@Mod(Resources.MOD_ID)
public class MCCord implements IMod {
    public final Logger LOGGER = LogManager.getLogger();
    private DiscordManager discordManager;
    private UserManager userManager;
    private DataManager dataManager;
    private ServerManager serverManager;
    private ChatManager chatManager;
    private EventManager eventManager;
    private CommandManager commandManager;
    private CommandSuggestions commandSuggestions;

    public MCCord() {
        this.discordManager = new DiscordManager(this);
        this.serverManager = new ServerManager(this);
        this.dataManager = new DataManager(this);

        this.userManager = new UserManager(this);
        this.eventManager = new EventManager(this);
        this.chatManager = new ChatManager(this);
        this.commandManager = new CommandManager(this);
        this.commandSuggestions = new CommandSuggestions(this);

        register();
    }

    /**
     * Constructor for JUnit.
     * 
     * @param discordManager
     * @param serverManager
     * @param dataManager
     */
    public MCCord(DiscordManager discordManager, ServerManager serverManager, DataManager dataManager) {
        this.discordManager = discordManager;
        this.serverManager = serverManager;
        this.dataManager = dataManager;

        this.userManager = new UserManager(this);
        this.eventManager = new EventManager(this);
        this.chatManager = new ChatManager(this);
        this.commandManager = new CommandManager(this);
        this.commandSuggestions = new CommandSuggestions(this);
    }

    private void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        eventManager.register(MinecraftForge.EVENT_BUS);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onServerStopped);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onCommandRegister);
    }

    private void onCommandRegister(final RegisterCommandsEvent event) {
        commandManager.register(event.getDispatcher());
    }

    private void onServerStarted(final FMLServerStartedEvent event) {
        serverManager.register(event.getServer());
        discordManager.register();
        dataManager.register();
    }

    private void onServerStopped(final FMLServerStoppedEvent event) {
        dataManager.unregister();
        discordManager.unregister();
        serverManager.unregister();
    }

    @Override
    public ChatManager getChatManager() {
        return chatManager;
    }

    @Override
    public DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public DiscordManager getDiscordManager() {
        return discordManager;
    }

    @Override
    public ServerManager getServerManager() {
        return serverManager;
    }

    @Override
    public CommandSuggestions getCommandSuggestions() {
        return commandSuggestions;
    }
}