package ca.concordia.mccord.entity;

import java.util.HashMap;
import java.util.UUID;

import ca.concordia.mccord.discord.DiscordManager;
import ca.concordia.mccord.world.ServerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class UserManager {
    private static final String DEFAULT_CHANNEL = "general";

    private static HashMap<String, String> DISCORD_MC_MAP = new HashMap<String, String>();
    private static HashMap<String, String> MC_DISCORD_MAP = new HashMap<String, String>();
    private static HashMap<String, String> USER_CHANNEL = new HashMap<String, String>();

    private static void insertUUID(String discordUUID, String mcUUID) {
        DISCORD_MC_MAP.put(discordUUID, mcUUID);
        MC_DISCORD_MAP.put(mcUUID, discordUUID);
    }

    public static void linkUsers(String discordUUID, String mcUUID) {
        insertUUID(discordUUID, mcUUID);
    }

    public static void linkUsers(ServerPlayerEntity player, User user) {
        String mcUUID = player.getUniqueID().toString();
        String discordUUID = user.getId();

        insertUUID(discordUUID, mcUUID);
    }

    public static ServerPlayerEntity getMCPlayer(String discordUUID) {
        if(!DISCORD_MC_MAP.containsKey(discordUUID)) {
            return null;
        }

        return fromMCUUID(DISCORD_MC_MAP.get(discordUUID));
    }

    public static User getDiscordUser(String mcUUID) {
        if(!MC_DISCORD_MAP.containsKey(mcUUID)) {
            return null;
        }

        return fromDiscordUUID(MC_DISCORD_MAP.get(mcUUID));
    }

    public static User getDiscordUser(PlayerEntity playerEntity) {
        return getDiscordUser(playerEntity.getUniqueID().toString());
    }

    public static boolean setCurrentChannel(PlayerEntity player, String channelName) {
        TextChannel textChannel = DiscordManager.getChannelByName(channelName);

        if (textChannel == null) {
            return false;
        }

        USER_CHANNEL.put(player.getUniqueID().toString(), channelName);

        return true;
    }

    public static String getCurrentChannel(PlayerEntity player) {
        String uuid = player.getUniqueID().toString();
        
        if (!USER_CHANNEL.containsKey(uuid)) {
            return DEFAULT_CHANNEL;
        }

        return USER_CHANNEL.get(uuid);
    }

    public static ServerPlayerEntity fromMCUUID(String uuid) {
        return ServerManager.getServer().getPlayerList().getPlayerByUUID(UUID.fromString(uuid));
    }

    public static ServerPlayerEntity fromMCName(String name) {
        return ServerManager.getServer().getPlayerList().getPlayerByUsername(name);
    }

    public static User fromDiscordUUID(String uuid) {
        return DiscordManager.getUserFromUUID(uuid);
    }

    public static User fromDiscordTag(String username, String discriminator) {
        return DiscordManager.getUserByTag(username, discriminator);
    }

    public static User fromDiscordTag(String tag) {
        return DiscordManager.getUserByTag(tag);
    }
}
