package ca.concordia.mccord.entity;

import java.util.Optional;
import java.util.UUID;

import ca.concordia.mccord.data.DataManager;
import ca.concordia.mccord.data.UserData;
import ca.concordia.mccord.utils.AbstractManager;
import ca.concordia.mccord.utils.IMod;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class UserManager extends AbstractManager {
    public UserManager(IMod mod) {
        super(mod);
    }

    public Optional<ServerPlayerEntity> fromMCUUID(String uuid) {
        try {
            return Optional.ofNullable(getMod().getServerManager().getServer().get().getPlayerList()
                    .getPlayerByUUID(UUID.fromString(uuid)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<ServerPlayerEntity> fromMCName(String name) {
        try {
            return Optional.ofNullable(
                    getMod().getServerManager().getServer().get().getPlayerList().getPlayerByUsername(name));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> fromDiscordUUID(String uuid) {
        return getMod().getDiscordManager().getUserFromUUID(uuid);
    }

    public Optional<User> fromDiscordTag(String username, String discriminator) {
        return getMod().getDiscordManager().getUserByTag(username, discriminator);
    }

    public Optional<User> fromDiscordTag(String tag) {
        return getMod().getDiscordManager().getUserByTag(tag);
    }

    public void link(ServerPlayerEntity playerEntity, User user) throws CommandException {
        link(Optional.ofNullable(playerEntity), Optional.ofNullable(user));
    }

    public void link(Optional<ServerPlayerEntity> oPlayerEntity, Optional<User> user) throws CommandException {
        PlayerEntity playerEntity = oPlayerEntity.get();

        String mcUUID = playerEntity.getUniqueID().toString();

        String discordUUID = user.get().getId();

        link(mcUUID, discordUUID);
    }

    /**
     */
    public void link(String mcUUID, String discordUUID) throws CommandException {
        if (getMod().getDataManager().containsUser(mcUUID, discordUUID)) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Account(s) already linked."));
        }

        getMod().getDataManager().setUserData(mcUUID, userData -> {
            userData.setDiscordUUID(discordUUID);
        });
    }

    /**
     * @param user
     * @return
     */
    public Optional<ServerPlayerEntity> getPlayerEntityFromDiscordUser(User user) {
        try {
            UserData userData = getMod().getDataManager().getUserData(user).get();

            return Optional.ofNullable(getMod().getServerManager().getServer().get().getPlayerList()
                    .getPlayerByUUID(UUID.fromString(userData.getMCUUID())));
        } catch (Exception e) {
            e.printStackTrace();

            return Optional.empty();
        }
    }

    /**
     * @param playerEntity
     * @return
     */
    public Optional<User> getDiscordUserFromPlayerEntity(PlayerEntity playerEntity) {
        DataManager dataManager = getMod().getDataManager();

        UserData userData = dataManager.getUserData(playerEntity).get();

        return getMod().getDiscordManager().getUserFromUUID(userData.getDiscordUUID());
    }
}
