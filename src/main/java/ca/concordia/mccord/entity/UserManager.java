package ca.concordia.mccord.entity;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import ca.concordia.mccord.discord.DiscordManager;
import ca.concordia.mccord.world.ServerManager;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class UserManager {
    private static final String DEFAULT_CHANNEL = "general";

    private static HashMap<String, String> DISCORD_MC_MAP = new HashMap<String, String>();
    private static HashMap<String, String> MC_DISCORD_MAP = new HashMap<String, String>();
    private static HashMap<String, String> USER_CHANNEL = new HashMap<String, String>();

    private static boolean insertUUID(String discordUUID, String mcUUID) {
        DISCORD_MC_MAP.put(discordUUID, mcUUID);
        MC_DISCORD_MAP.put(mcUUID, discordUUID);

        return true;
    }

    public static boolean linkUsers(String discordUUID, String mcUUID) {
        return insertUUID(discordUUID, mcUUID);
    }

    public static boolean linkUsers(Optional<ServerPlayerEntity> player, Optional<User> user) {
        try {
            String mcUUID = player.get().getUniqueID().toString();
            String discordUUID = user.get().getId();

            return linkUsers(discordUUID, mcUUID);
        } catch (Exception e) {
            return false;
        }
    }

    public static Optional<ServerPlayerEntity> getMCPlayer(String discordUUID) {
        try {
            return fromMCUUID(DISCORD_MC_MAP.get(discordUUID));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<User> getDiscordUser(String mcUUID) {
        try {
            return fromDiscordUUID(MC_DISCORD_MAP.get(mcUUID));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<User> getDiscordUser(PlayerEntity playerEntity) {
        return getDiscordUser(playerEntity.getUniqueID().toString());
    }

    public static boolean setCurrentChannel(PlayerEntity player, String channelName) {
        try {
            USER_CHANNEL.put(player.getUniqueID().toString(),
                    DiscordManager.getChannelByName(channelName).get().getName());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static String getCurrentChannel(PlayerEntity player) {
        return Optional.ofNullable(USER_CHANNEL.get(player.getUniqueID().toString())).orElse(DEFAULT_CHANNEL);
    }

    public static Optional<ServerPlayerEntity> fromMCUUID(String uuid) {
        try {
            return Optional
                    .ofNullable(ServerManager.getServer().get().getPlayerList().getPlayerByUUID(UUID.fromString(uuid)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<ServerPlayerEntity> fromMCName(String name) {
        try {
            return Optional.ofNullable(ServerManager.getServer().get().getPlayerList().getPlayerByUsername(name));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<User> fromDiscordUUID(String uuid) {
        return DiscordManager.getUserFromUUID(uuid);
    }

    public static Optional<User> fromDiscordTag(String username, String discriminator) {
        return DiscordManager.getUserByTag(username, discriminator);
    }

    public static Optional<User> fromDiscordTag(String tag) {
        return DiscordManager.getUserByTag(tag);
    }
}
