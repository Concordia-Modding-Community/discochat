package ca.concordia.mccord.server;

import ca.concordia.mccord.utils.AbstractManager;
import ca.concordia.mccord.utils.IMod;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigManager extends AbstractManager {
    private ForgeConfigSpec configs;

    private final String CATEGORY_GENERAL = "general";

    private ForgeConfigSpec.ConfigValue<String> MC_COMMAND_PREFIX;

    private ForgeConfigSpec.ConfigValue<String> DATA_LOCATION;

    private final String CATEGORY_DISCORD = "discord";

    private ForgeConfigSpec.ConfigValue<String> DISCORD_API_KEY;

    private ForgeConfigSpec.ConfigValue<String> DEFAULT_CHANNEL;

    private ForgeConfigSpec.ConfigValue<String> DISCORD_COMMAND_PREFIX;

    private final String CATEGORY_DISCORD_ROLE = "role";

    private ForgeConfigSpec.ConfigValue<String> DISCORD_ADMIN_ROLE;

    public ConfigManager(IMod mod) {
        super(mod);

        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        buildGeneralConfig(builder);

        buildDiscordConfig(builder);

        this.configs = builder.build();
    }

    private void buildGeneralConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("General Settings").push(CATEGORY_GENERAL);

        // TODO: Better save location.
        DATA_LOCATION = builder.comment("MCCord NBT Data Location").define("dataLocation", "./data/mccord.dat");

        MC_COMMAND_PREFIX = builder.comment("MCCord Command Prefix").define("commandPrefix", "discord");

        builder.pop();
    }

    /**
     * Creates the Discord TOML config.
     * 
     * @param builder
     */
    private void buildDiscordConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Discord Settings").push(CATEGORY_DISCORD);

        DISCORD_API_KEY = builder.comment("Discord Bot API Key").define("apiKey", "");

        DEFAULT_CHANNEL = builder.comment("Minecraft Default Discord Channel").define("defaultChannel", "general");

        DISCORD_COMMAND_PREFIX = builder.comment("Discord Command Prefix").define("commandPrefix", "!");

        buildDiscordRolesConfig(builder);

        builder.pop();
    }

    private void buildDiscordRolesConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Discord Roles").push(CATEGORY_DISCORD_ROLE);

        DISCORD_ADMIN_ROLE = builder.comment("Discord Admin Role").define("adminRole", "administrator");

        builder.pop();
    }

    /**
     * TODO: Add hidden Discord channels.
     * 
     * @param channel
     * @return
     */
    public boolean isChannelAccessible(MessageChannel channel) {
        return true;
    }

    public void setDiscordToken(String token) {
        DISCORD_API_KEY.set(token);
    }

    public String getDiscordToken() {
        return DISCORD_API_KEY.get();
    }

    public void setDefaultChannel(String channelName) {
        DEFAULT_CHANNEL.set(channelName);
    }

    public String getDefaultChannel() {
        return DEFAULT_CHANNEL.get();
    }

    public ForgeConfigSpec getServerConfigs() {
        return configs;
    }

    public void setDiscordCommandPrefix(String prefix) {
        DISCORD_COMMAND_PREFIX.set(prefix);
    }

    public String getDiscordCommandPrefix() {
        return DISCORD_COMMAND_PREFIX.get();
    }

    public String getMCCommandPrefix() {
        return MC_COMMAND_PREFIX.get();
    }

    public void setMCCommandPrefix(String prefix) {
        MC_COMMAND_PREFIX.set(prefix);
    }

    public String getDiscordAdminRole() {
        return DISCORD_ADMIN_ROLE.get();
    }

    public String getDataLocation() {
        return DATA_LOCATION.get();
    }

    public boolean isDiscordTokenValid() {
        return !getDiscordToken().isBlank();
    }
}
