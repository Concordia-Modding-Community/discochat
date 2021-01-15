package ca.concordia.discochat.server;

import ca.concordia.discochat.utils.AbstractManager;
import ca.concordia.discochat.utils.IMod;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.minecraft.util.text.Color;
import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigManager extends AbstractManager {
    private ForgeConfigSpec configs;

    private final String CATEGORY_GENERAL = "general";

    private ForgeConfigSpec.ConfigValue<String> MC_COMMAND_PREFIX;

    private ForgeConfigSpec.ConfigValue<String> MENTION_COLOR_HEX;

    private ForgeConfigSpec.ConfigValue<String> DATA_LOCATION;

    private ForgeConfigSpec.ConfigValue<String> MC_TEXT_FORMAT;

    private final String CATEGORY_DISCORD = "discord";

    private ForgeConfigSpec.ConfigValue<String> DISCORD_API_KEY;

    private ForgeConfigSpec.ConfigValue<String> DEFAULT_CHANNEL;

    private ForgeConfigSpec.ConfigValue<String> NOTIFICATION_CHANNEL;

    private ForgeConfigSpec.ConfigValue<String> DISCORD_COMMAND_PREFIX;

    private ForgeConfigSpec.ConfigValue<String> DISCORD_TEXT_FORMAT;

    private final String CATEGORY_DISCORD_ROLE = "role";

    private ForgeConfigSpec.ConfigValue<String> DISCORD_ADMIN_ROLE;

    private final String CATEGORY_DISCORD_MESSAGE = "message";

    private ForgeConfigSpec.ConfigValue<String> DISCORD_PLAYER_JOIN;

    private ForgeConfigSpec.ConfigValue<String> DISCORD_PLAYER_LEFT;

    private ForgeConfigSpec.ConfigValue<String> DISCORD_PLAYER_ADVANCEMENT;

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
        DATA_LOCATION = builder.comment("DiscoChat NBT Data Location (relative to server path)").define("dataLocation",
                "./config/discochat.dat");

        MENTION_COLOR_HEX = builder.comment("DiscoChat Discord Mention Hex Color").define("mentionColor", "#7289da");

        MC_COMMAND_PREFIX = builder.comment("DiscoChat Command Prefix").define("commandPrefix", "discord");

        MC_TEXT_FORMAT = builder.comment("DiscoChat Ingame Text Format (@c = command, @p = player, @m = message)")
                .define("textFormat", "<@c | @p> @m");

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

        DEFAULT_CHANNEL = builder.comment("Default channel for Minecraft chat").define("defaultChannel", "general");

        NOTIFICATION_CHANNEL = builder.comment("Channel for Minecraft notification").define("notificationChannel",
                "general");

        DISCORD_COMMAND_PREFIX = builder.comment("Discord Command Prefix").define("commandPrefix", "!");

        DISCORD_TEXT_FORMAT = builder.comment("Discord Command Text Format (@p = player, @m = message)")
                .define("textFormat", "@p  @m");

        buildDiscordRolesConfig(builder);

        buildDiscordMessagesConfig(builder);

        builder.pop();
    }

    private void buildDiscordRolesConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Discord Roles").push(CATEGORY_DISCORD_ROLE);

        DISCORD_ADMIN_ROLE = builder.comment("Discord Admin Role").define("adminRole", "administrator");

        builder.pop();
    }

    private void buildDiscordMessagesConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Discord Messags").push(CATEGORY_DISCORD_MESSAGE);

        DISCORD_PLAYER_JOIN = builder.comment("Message for Player Joining (@p = player)").define("playerJoin",
                "@p joined.");

        DISCORD_PLAYER_LEFT = builder.comment("Message for Player Leaving (@p = player)").define("playerLeft",
                "@p left.");

        DISCORD_PLAYER_ADVANCEMENT = builder.comment(
                "Message for Player Advancement (@p = player, @t = advancement title, @d = advancement description)")
                .define("playerAdvancement", "@p has made the advancement **@t**\n*@d*");

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

    public Color getMentionColor() {
        return Color.fromHex(MENTION_COLOR_HEX.get());
    }

    public String getMCTextFormat() {
        return MC_TEXT_FORMAT.get();
    }

    public String getDiscordTextFormat() {
        return DISCORD_TEXT_FORMAT.get();
    }

    public String getNotificationChannel() {
        return NOTIFICATION_CHANNEL.get();
    }

    public String getPlayerJoinMessage() {
        return DISCORD_PLAYER_JOIN.get();
    }

    public String getPlayerLeaveMessage() {
        return DISCORD_PLAYER_LEFT.get();
    }

    public String getPlayerAdvancementMessage() {
        return DISCORD_PLAYER_ADVANCEMENT.get();
    }

    public boolean isDiscordTokenValid() {
        return !getDiscordToken().isBlank();
    }
}
