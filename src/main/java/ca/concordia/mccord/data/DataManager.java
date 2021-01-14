package ca.concordia.mccord.data;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

import ca.concordia.mccord.utils.AbstractManager;
import ca.concordia.mccord.utils.IMod;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraftforge.common.util.INBTSerializable;

public class DataManager extends AbstractManager implements INBTSerializable<CompoundNBT> {
    private static final String PLAYER_DATA = "playerData";
    public CompoundNBT playerDataNBT;
    private static final String DISCORD_MAPPING = "discordUUID";
    public CompoundNBT discordMappingNBT;

    public DataManager(IMod mod) {
        super(mod);
        this.playerDataNBT = new CompoundNBT();
        this.discordMappingNBT = new CompoundNBT();
    }

    public DataManager register() {
        loadUserData();

        return this;
    }

    public void unregister() {
        saveUserData();
    }

    public boolean containsUser(User user) {
        return containsUserDiscord(user.getId());
    }

    public boolean containsUser(PlayerEntity playerEntity) {
        return containsUserMC(playerEntity.getUniqueID().toString());
    }

    public boolean containsUser(String mcUUID, String discordUUID) {
        return containsUserMC(mcUUID) || containsUserDiscord(discordUUID);
    }

    private boolean containsUserMC(String mcUUID) {
        return playerDataNBT.contains(mcUUID);
    }

    private boolean containsUserDiscord(String discordUUID) {
        return discordMappingNBT.contains(discordUUID);
    }

    public Optional<UserData> getUserData(User user) {
        return getUserDataDiscord(user.getId());
    }

    public Optional<UserData> getUserData(PlayerEntity playerEntity) {
        return getUserDataMC(playerEntity.getUniqueID().toString());
    }

    private Optional<UserData> getUserDataDiscord(String discordUUID) {
        if (!discordMappingNBT.contains(discordUUID)) {
            return Optional.empty();
        }

        return getUserDataMC(discordMappingNBT.getString(discordUUID));
    }

    /**
     * @param mcUUID
     * @return
     */
    private Optional<UserData> getUserDataMC(String mcUUID) {
        if (!playerDataNBT.contains(mcUUID)) {
            return Optional.empty();
        }

        CompoundNBT nbt = playerDataNBT.getCompound(mcUUID);

        return Optional.of(new UserData(nbt));
    }

    public void setUserData(String mcUUID, Consumer<UserData> function) {
        Optional<UserData> oUserData = getUserDataMC(mcUUID);

        UserData userData;

        if (oUserData.isPresent()) {
            userData = oUserData.get();
        } else {
            userData = new UserData();
        }

        function.accept(userData);

        userData.setMCUUID(mcUUID);

        if (!userData.getDiscordUUID().isBlank()) {
            discordMappingNBT.putString(userData.getDiscordUUID(), mcUUID);
        }

        playerDataNBT.put(mcUUID, userData.serializeNBT());

        // TODO: Better saving?
        saveUserData();
    }

    private void loadUserData() {
        File file = new File(getMod().getConfigManager().getDataLocation());

        if (!file.exists()) {
            return;
        }

        try {
            CompoundNBT nbt = CompressedStreamTools.read(file);

            deserializeNBT(nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveUserData() {
        File file = new File(getMod().getConfigManager().getDataLocation());

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            CompressedStreamTools.write(serializeNBT(), file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.put(PLAYER_DATA, playerDataNBT);
        nbt.put(DISCORD_MAPPING, discordMappingNBT);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        playerDataNBT = nbt.getCompound(PLAYER_DATA);
        discordMappingNBT = nbt.getCompound(DISCORD_MAPPING);
    }
}
