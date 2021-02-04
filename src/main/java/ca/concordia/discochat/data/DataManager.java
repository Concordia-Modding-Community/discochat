package ca.concordia.discochat.data;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Consumer;

import ca.concordia.discochat.DiscoChatMod;
import ca.concordia.discochat.data.UserData.VerifyStatus;
import ca.concordia.discochat.utils.AbstractManager;
import ca.concordia.discochat.utils.IMod;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.INBTSerializable;

public class DataManager extends AbstractManager implements INBTSerializable<CompoundNBT> {
    private static final String PLAYER_DATA = "playerData";
    public CompoundNBT playerDataNBT;
    private static final String DISCORD_MAPPING = "discordUUID";
    public CompoundNBT discordMappingNBT;
    private File baseDirectory;

    public DataManager(IMod mod) {
        super(mod);
        this.playerDataNBT = new CompoundNBT();
        this.discordMappingNBT = new CompoundNBT();
    }

    public DataManager register(MinecraftServer server) {
        this.baseDirectory = server.getDataDirectory();

        loadUserData();

        return this;
    }

    public void unregister() {
        saveUserData();
    }

    public boolean containsUserVerified(User user) {
        return containsUserDiscordVerified(user.getId());
    }

    public boolean containsUserVerified(PlayerEntity playerEntity) {
        return containsUserMCVerified(playerEntity.getUniqueID().toString());
    }

    public boolean containsUserVerified(String mcUUID, String discordUUID) {
        return containsUserMCVerified(mcUUID) || containsUserDiscordVerified(discordUUID);
    }

    public boolean containsUserDiscordVerified(String discordUUID) {
        if (!discordMappingNBT.contains(discordUUID)) {
            return false;
        }

        return containsUserMCVerified(discordMappingNBT.getString(discordUUID));
    }

    private boolean containsUserMCVerified(String mcUUID) {
        if (!playerDataNBT.contains(mcUUID)) {
            return false;
        }

        UserData userData = getUserDataMC(mcUUID).get();

        return userData.getVerifyStatus() == VerifyStatus.BOTH;
    }

    public Optional<UserData> getUserData(User user) {
        return getUserDataDiscord(user.getId());
    }

    public boolean removeUserDataMC(String mcUUID) {
        if (!playerDataNBT.contains(mcUUID)) {
            return false;
        }

        UserData userData = getUserDataMC(mcUUID).get();

        playerDataNBT.remove(mcUUID);

        discordMappingNBT.remove(userData.getDiscordUUID());

        saveUserData();

        return true;
    }

    public Optional<UserData> getUserData(PlayerEntity playerEntity) {
        return getUserDataMC(playerEntity.getUniqueID().toString());
    }

    public Optional<UserData> getUserDataDiscord(String discordUUID) {
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

        if (!userData.getDiscordUUID().isEmpty()) {
            discordMappingNBT.putString(userData.getDiscordUUID(), mcUUID);
        }

        playerDataNBT.put(mcUUID, userData.serializeNBT());

        // TODO: Better saving?
        saveUserData();
    }

    private File getDataFile() {
        return new File(getMod().getConfigManager().getDataLocation());
    }

    public void loadUserData() {
        File file = getDataFile();

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

    public void saveUserData() {
        File file = getDataFile();

        try {
            if (!file.exists()) {
                File parent = file.getParentFile();

                if (parent.isDirectory()) {
                    parent.getParentFile().mkdirs();
                }
                
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
