package ca.concordia.mccord;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final String CATEGORY_DISCORD = "discord";

    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.ConfigValue<String> DISCORD_API_KEY;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        buildDiscordConfig(SERVER_BUILDER);

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    /**
     * Creates the Discord TOML config.
     * @param serverBuilder
     */
    public static void buildDiscordConfig(ForgeConfigSpec.Builder serverBuilder) {
        serverBuilder.comment("Discord Settings").push(CATEGORY_DISCORD);

        DISCORD_API_KEY = serverBuilder.comment("Discord Bot API Key").define("apiKey", "");

        serverBuilder.pop();
    }
}
