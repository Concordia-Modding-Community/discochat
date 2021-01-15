package ca.concordia.discochat;

import ca.concordia.discochat.chat.ChatManager;
import ca.concordia.discochat.commands.CommandManager;
import ca.concordia.discochat.commands.CommandSuggestions;
import ca.concordia.discochat.data.DataManager;
import ca.concordia.discochat.discord.DiscordManager;
import ca.concordia.discochat.entity.UserManager;
import ca.concordia.discochat.events.EventManager;
import ca.concordia.discochat.server.ConfigManager;
import ca.concordia.discochat.server.ServerManager;
import ca.concordia.discochat.utils.IMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

@Mod(Resources.MOD_ID)
public class DiscoChatMod implements IMod {
    private DiscordManager discordManager;
    private UserManager userManager;
    private DataManager dataManager;
    private ServerManager serverManager;
    private ChatManager chatManager;
    private EventManager eventManager;
    private CommandManager commandManager;
    private CommandSuggestions commandSuggestions;
    private ConfigManager configManager;

    public DiscoChatMod() {
        this.discordManager = new DiscordManager(this);
        this.serverManager = new ServerManager(this);
        this.dataManager = new DataManager(this);
        this.configManager = new ConfigManager(this);

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
    public DiscoChatMod(DiscordManager discordManager, ServerManager serverManager, DataManager dataManager, ConfigManager configManager) {
        this.discordManager = discordManager;
        this.serverManager = serverManager;
        this.dataManager = dataManager;
        this.configManager = configManager;

        this.userManager = new UserManager(this);
        this.eventManager = new EventManager(this);
        this.chatManager = new ChatManager(this);
        this.commandManager = new CommandManager(this);
        this.commandSuggestions = new CommandSuggestions(this);
    }

    private void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, configManager.getServerConfigs());

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
        dataManager.register(event.getServer());
        discordManager.register();
    }

    private void onServerStopped(final FMLServerStoppedEvent event) {
        discordManager.unregister();
        dataManager.unregister();
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

    @Override
    public ConfigManager getConfigManager() {
        return configManager;
    }
}