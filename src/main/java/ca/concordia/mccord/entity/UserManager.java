package ca.concordia.mccord.entity;

import java.util.Optional;
import java.util.UUID;

import ca.concordia.mccord.discord.DiscordManager;
import ca.concordia.mccord.utils.DataManager;
import ca.concordia.mccord.utils.ServerManager;
import ca.concordia.mccord.utils.UserData;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class UserManager {
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

    public static void link(ServerPlayerEntity playerEntity, User user) throws CommandException {
        link(Optional.ofNullable(playerEntity), Optional.ofNullable(user));
    }

    public static void link(Optional<ServerPlayerEntity> oPlayerEntity, Optional<User> user) throws CommandException {
        PlayerEntity playerEntity = oPlayerEntity.get();

        String mcUUID = playerEntity.getUniqueID().toString();

        String discordUUID = user.get().getId();

        link(mcUUID, discordUUID);
    }

    /**
     */
    public static void link(String mcUUID, String discordUUID) throws CommandException {
        if (DataManager.containsUser(mcUUID, discordUUID)) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Account(s) already linked."));
        }

        DataManager.setUserData(mcUUID, userData -> {
            userData.discordUUID = discordUUID;
        });
    }

    /**
     * @param user
     * @return
     */
    public static Optional<ServerPlayerEntity> getPlayerEntityFromDiscordUser(Optional<User> user) {
        try {
            UserData userData = DataManager.getUserData(user.get()).get();

            return Optional.ofNullable(
                    ServerManager.getServer().get().getPlayerList().getPlayerByUUID(UUID.fromString(userData.mcUUID)));
        } catch (Exception e) {
            e.printStackTrace();

            return Optional.empty();
        }
    }

    /**
     * @param playerEntity
     * @return
     */
    public static Optional<User> getDiscordUserFromPlayerEntity(Optional<ServerPlayerEntity> playerEntity) {
        try {
            UserData userData = DataManager.getUserData(playerEntity.get()).get();

            return DiscordManager.getUserFromUUID(userData.discordUUID);
        } catch (Exception e) {
            e.printStackTrace();

            return Optional.empty();
        }
    }
}
