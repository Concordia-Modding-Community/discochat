package ca.concordia.mccord.utils;

import java.io.File;
import java.util.Optional;
import java.util.function.Consumer;

import ca.concordia.mccord.Config;
import ca.concordia.mccord.Resources;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

@Mod.EventBusSubscriber(modid = Resources.MOD_ID, bus = Bus.FORGE)
public class DataManager implements INBTSerializable<CompoundNBT> {
    private static final String PLAYER_DATA = "playerData";
    private static CompoundNBT playerDataNBT = new CompoundNBT();
    private static final String DISCORD_MAPPING = "discordUUID";
    private static CompoundNBT discordMappingNBT = new CompoundNBT();

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent event) {
        loadUserData();
    }

    public static boolean containsUser(User user) {
        return containsUserDiscord(user.getId());
    }

    public static boolean containsUser(PlayerEntity playerEntity) {
        return containsUserMC(playerEntity.getUniqueID().toString());
    }

    public static boolean containsUser(String mcUUID, String discordUUID) {
        return containsUserMC(mcUUID) || containsUserDiscord(discordUUID);
    }

    private static boolean containsUserMC(String mcUUID) {
        return playerDataNBT.contains(mcUUID);
    }

    private static boolean containsUserDiscord(String discordUUID) {
        return discordMappingNBT.contains(discordUUID);
    }

    public static Optional<UserData> getUserData(User user) {
        return getUserDataDiscord(user.getId());
    }

    public static Optional<UserData> getUserData(PlayerEntity playerEntity) {
        return getUserDataMC(playerEntity.getUniqueID().toString());
    }

    private static Optional<UserData> getUserDataDiscord(String discordUUID) {
        if(!discordMappingNBT.contains(discordUUID)) {
            return Optional.empty();
        }

        return getUserDataMC(discordMappingNBT.getString(discordUUID));
    }

    /**
     * @param mcUUID
     * @return
     */
    private static Optional<UserData> getUserDataMC(String mcUUID) {
        if(!playerDataNBT.contains(mcUUID)) {
            return Optional.empty();
        }

        CompoundNBT nbt = playerDataNBT.getCompound(mcUUID);

        return Optional.of(new UserData(nbt));
    }

    public static void setUserData(String mcUUID, Consumer<UserData> function) {
        Optional<UserData> oUserData = getUserDataMC(mcUUID);

        UserData userData;

        if (oUserData.isPresent()) {
            userData = oUserData.get();
        } else {
            userData = new UserData();
        }

        function.accept(userData);

        userData.mcUUID = mcUUID;

        if(!userData.discordUUID.isBlank()) {
            discordMappingNBT.putString(userData.discordUUID, mcUUID);
        }

        playerDataNBT.put(mcUUID, userData.serializeNBT());

        // TODO: Better saving?
        saveUserData();
    }

    private static void loadUserData() {
        File file = new File(Config.DATA_LOCATION.get());

        if (!file.exists()) {
            return;
        }

        try {
            CompoundNBT nbt = CompressedStreamTools.read(file);

            sDeserializeNBT(nbt);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveUserData() {
        File file = new File(Config.DATA_LOCATION.get());

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            CompressedStreamTools.write(sSerializeNBT(), file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void sDeserializeNBT(CompoundNBT nbt) {
        new DataManager().deserializeNBT(nbt);
    }

    public static CompoundNBT sSerializeNBT() {
        return new DataManager().serializeNBT();
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
