package ca.concordia.discochat.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class UserData implements INBTSerializable<CompoundNBT> {
    public static final String ANY_CHANNEL = "any-channel";

    private static final String MC_UUID = "mcUUID";
    private String mcUUID = "";

    private static final String DISCORD_UUID = "discordUUID";
    private String discordUUID = "";

    private static final String CURRENT_CHANNEL = "currentChannel";
    private String currentChannel = "";

    private static final String HIDDEN_CHANNELS = "channelVisibility";
    private HashSet<String> hiddenChannels = new HashSet<String>();

    public static enum VerifyStatus {
        NONE, MINECRAFT, DISCORD, BOTH;

        public static VerifyStatus fromInt(int value) {
            switch (value) {
                case 0:
                    return NONE;
                case 1:
                    return MINECRAFT;
                case 2:
                    return DISCORD;
                case 3:
                    return BOTH;
                default:
                    return null;
            }
        }

        public int toInt() {
            switch(this) {
                case NONE:
                    return 0;
                case MINECRAFT:
                    return 1;
                case DISCORD:
                    return 2;
                case BOTH:
                    return 3;
                default:
                    return 0;
            }
        }
    }

    private static final String VERIFY_STATUS = "verify";
    private VerifyStatus verifyStatus = VerifyStatus.NONE;

    public UserData() {
    }

    public UserData(CompoundNBT nbt) {
        this.deserializeNBT(nbt);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            new ObjectOutputStream(os).writeObject(hiddenChannels);

            nbt.putByteArray(HIDDEN_CHANNELS, os.toByteArray());
        } catch(Exception e) {
            e.printStackTrace();
        }

        nbt.putString(DISCORD_UUID, discordUUID);
        nbt.putString(MC_UUID, mcUUID);
        nbt.putString(CURRENT_CHANNEL, currentChannel);
        nbt.putInt(VERIFY_STATUS, verifyStatus.toInt());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        byte[] channelVisiblityBytes = nbt.getByteArray(HIDDEN_CHANNELS);

        if (channelVisiblityBytes.length > 0) {
            try {
                ByteArrayInputStream is = new ByteArrayInputStream(channelVisiblityBytes);
                Object obj = new ObjectInputStream(is).readObject();

                if(obj instanceof HashSet<?>) {
                    this.hiddenChannels = (HashSet<String>) obj;
                } else {
                    throw new Exception("Not at hashset of channels.");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        this.discordUUID = nbt.getString(DISCORD_UUID);
        this.mcUUID = nbt.getString(MC_UUID);
        this.currentChannel = nbt.getString(CURRENT_CHANNEL);
        this.verifyStatus = VerifyStatus.fromInt(nbt.getInt(VERIFY_STATUS));
    }

    public String getMCUUID() {
        return mcUUID;
    }

    public String getDiscordUUID() {
        return discordUUID;
    }

    public String getCurrentChannel() {
        return currentChannel;
    }

    public HashSet<String> getHiddenChannels() {
        return hiddenChannels;
    }

    public void setMCUUID(String mcUUID) {
        this.mcUUID = mcUUID;
    }

    public void setDiscordUUID(String discordUUID) {
        this.discordUUID = discordUUID;
    }

    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }

    public void setHiddenChannels(HashSet<String> hiddenChannels) {
        this.hiddenChannels = hiddenChannels;
    }

    public VerifyStatus getVerifyStatus() {
        return this.verifyStatus;
    }

    public void setVerifyStatus(VerifyStatus verifyStatus) {
        this.verifyStatus = verifyStatus;
    }
}
