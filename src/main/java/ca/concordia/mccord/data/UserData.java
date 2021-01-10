package ca.concordia.mccord.data;

import java.util.HashSet;

import org.apache.commons.lang3.SerializationUtils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class UserData implements INBTSerializable<CompoundNBT> {
    public static final String ANY_CHANNEL = "any-channel";

    private static final String MC_UUID = "mcUUID";
    public String mcUUID = "";

    private static final String DISCORD_UUID = "discordUUID";
    public String discordUUID = "";

    private static final String CURRENT_CHANNEL = "currentChannel";
    public String currentChannel = "";

    private static final String HIDDEN_CHANNELS = "channelVisibility";
    public HashSet<String> hiddenChannels = new HashSet<String>();
    
    public UserData() {}

    public UserData(CompoundNBT nbt) {
        this.deserializeNBT(nbt);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putByteArray(HIDDEN_CHANNELS, SerializationUtils.serialize(hiddenChannels));
        nbt.putString(DISCORD_UUID, discordUUID);
        nbt.putString(MC_UUID, mcUUID);
        nbt.putString(CURRENT_CHANNEL, currentChannel);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        byte[] channelVisiblityBytes = nbt.getByteArray(HIDDEN_CHANNELS);

        if(channelVisiblityBytes.length > 0) {
            this.hiddenChannels = SerializationUtils.deserialize(channelVisiblityBytes);
        }
        
        this.discordUUID = nbt.getString(DISCORD_UUID);
        this.mcUUID = nbt.getString(MC_UUID);
        this.currentChannel = nbt.getString(CURRENT_CHANNEL);
    }
}
