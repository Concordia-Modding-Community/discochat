package ca.concordia.mccord;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static ForgeConfigSpec SERVER_CONFIG;

    public static final String CATEGORY_GENERAL = "general";

    public static ForgeConfigSpec.ConfigValue<String> MC_COMMAND_PREFIX;

    public static ForgeConfigSpec.ConfigValue<String> DATA_LOCATION;

    public static final String CATEGORY_DISCORD = "discord";

    public static ForgeConfigSpec.ConfigValue<String> DISCORD_API_KEY;

    public static ForgeConfigSpec.ConfigValue<String> DEFAULT_CHANNEL;

    public static ForgeConfigSpec.ConfigValue<String> DISCORD_COMMAND_PREFIX;

    public static final String CATEGORY_DISCORD_ROLE = "role";

    public static ForgeConfigSpec.ConfigValue<String> DISCORD_ADMIN_ROLE;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        buildGeneralConfig(SERVER_BUILDER);

        buildDiscordConfig(SERVER_BUILDER);

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    public static void buildGeneralConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("General Settings").push(CATEGORY_GENERAL);

        // TODO: Better save location.
        DATA_LOCATION = builder.comment("MCCord NBT Data Location").define("dataLocation", "./data/mccord.dat");

        MC_COMMAND_PREFIX = builder.comment("MCCord Command Prefix").define("commandPrefix", "discord");

        builder.pop();
    }

    /**
     * Creates the Discord TOML config.
     * @param builder
     */
    public static void buildDiscordConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Discord Settings").push(CATEGORY_DISCORD);

        DISCORD_API_KEY = builder.comment("Discord Bot API Key").define("apiKey", "");

        DEFAULT_CHANNEL = builder.comment("Minecraft Default Discord Channel").define("defaultChannel", "general");

        DISCORD_COMMAND_PREFIX = builder.comment("Discord Command Prefix").define("commandPrefix", "!");

        buildDiscordRolesConfig(builder);

        builder.pop();
    }

    public static void buildDiscordRolesConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Discord Roles").push(CATEGORY_DISCORD_ROLE);

        DISCORD_ADMIN_ROLE = builder.comment("Discord Admin Role").define("adminRole", "administrator");

        builder.pop();
    }

    /**
     * TODO: Add hidden Discord channels.
     * @param channel
     * @return
     */
    public static boolean isChannelAccessible(MessageChannel channel) {
        return true;
    }
}
