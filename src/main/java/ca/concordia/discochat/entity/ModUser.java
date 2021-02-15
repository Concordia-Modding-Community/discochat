package ca.concordia.discochat.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import ca.concordia.discochat.chat.ChatMessage;
import ca.concordia.discochat.data.UserData;
import ca.concordia.discochat.data.UserData.VerifyStatus;
import ca.concordia.discochat.utils.IMod;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;

public class ModUser {
    private ServerPlayerEntity playerEntity;
    private User user;
    private IMod mod;

    private ModUser(IMod mod, Optional<ServerPlayerEntity> playerEntity, Optional<User> user) throws Exception {
        this.mod = mod;

        if (playerEntity.isPresent() && user.isPresent()) {
            this.playerEntity = playerEntity.get();
            this.user = user.get();
        } else if (playerEntity.isPresent()) {
            this.playerEntity = playerEntity.get();
            this.user = mod.getUserManager().getDiscordUserFromPlayerEntity(playerEntity.get()).get();
        } else if (user.isPresent()) {
            this.playerEntity = mod.getUserManager().getPlayerEntityFromDiscordUser(user.get()).get();
            this.user = user.get();
        } else {
            throw new Exception("Constructing empty ModUser.");
        }
    }

    protected IMod getMod() {
        return this.mod;
    }

    public ModUser(ServerPlayerEntity playerEntity, User user) {
        this.playerEntity = playerEntity;
        this.user = user;
    }

    private static Optional<ModUser> fromOptional(IMod mod, Optional<ServerPlayerEntity> playerEntity,
            Optional<User> user) {
        try {
            return Optional.ofNullable(new ModUser(mod, playerEntity, user));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<ModUser> fromDiscordUUID(IMod mod, String discordUUID) {
        return fromOptional(mod, Optional.empty(), mod.getUserManager().fromDiscordUUID(discordUUID));
    }

    public static Optional<ModUser> fromDiscordTag(IMod mod, String tag) {
        return fromOptional(mod, Optional.empty(), mod.getUserManager().fromDiscordTag(tag));
    }

    public static Optional<ModUser> fromDiscordTag(IMod mod, String username, String discriminator) {
        return fromOptional(mod, Optional.empty(), mod.getUserManager().fromDiscordTag(username, discriminator));
    }

    public static Optional<ModUser> fromDiscordUser(IMod mod, User user) {
        return fromDiscordUser(mod, Optional.ofNullable(user));
    }

    public static Optional<ModUser> fromDiscordUser(IMod mod, Optional<User> user) {
        return fromOptional(mod, Optional.empty(), user);
    }

    public static Optional<ModUser> fromMCName(IMod mod, String name) {
        return fromOptional(mod, mod.getUserManager().fromMCName(name), Optional.empty());
    }

    public static Optional<ModUser> fromMCUUID(IMod mod, String uuid) {
        return fromOptional(mod, mod.getUserManager().fromMCUUID(uuid), Optional.empty());
    }

    public static Optional<ModUser> fromMCPlayerEntity(IMod mod, Optional<ServerPlayerEntity> playerEntity) {
        return fromOptional(mod, playerEntity, Optional.empty());
    }

    public static Optional<ModUser> fromMCPlayerEntity(IMod mod, ServerPlayerEntity playerEntity) {
        return fromMCPlayerEntity(mod, Optional.ofNullable(playerEntity));
    }

    private Optional<UserData> getUserData() {
        Optional<UserData> userData = getMod().getDataManager().getUserData(playerEntity);

        if (!userData.isPresent() || userData.get().getVerifyStatus() != VerifyStatus.BOTH) {
            return Optional.empty();
        }

        return userData;
    }

    private void setUserData(Consumer<UserData> function) {
        getMod().getDataManager().setUserData(this.getMCUUID(), function);
    }

    /**
     * Gets the name of the current channel that the user is on.
     * 
     * @return
     */
    public String getCurrentChannel() {
        String channel = this.getUserData().get().getCurrentChannel();

        if (channel.isEmpty()) {
            return getMod().getConfigManager().getDefaultChannel();
        }

        return channel;
    }

    /**
     * @return
     */
    public void setCurrentChannel(String channelName) {
        this.setUserData(userData -> {
            userData.setCurrentChannel(channelName);
        });
    }

    /**
     * @return
     */
    public void setChannelHidden(String channelName) {
        this.setUserData(userData -> {
            userData.getHiddenChannels().add(channelName);
        });
    }

    public void setChannelVisible(String channelName) {
        this.setUserData(userData -> {
            userData.getHiddenChannels().remove(channelName);
        });
    }

    /**
     * @return
     */
    public void setAllChannelVisible() {
        this.setUserData(userData -> {
            userData.setHiddenChannels(new HashSet<>());
        });
    }

    /**
     * @return
     */
    public void setNoChannelVisible() {
        this.setUserData(userData -> {
            userData.setHiddenChannels(new HashSet<>());

            userData.getHiddenChannels().add(UserData.ANY_CHANNEL);
        });
    }

    /**
     * Checks if user has Discord permissions to server.
     * 
     * @param channel
     * @return
     */
    public boolean isChannelAccessible(MessageChannel channel) {
        return getMod().getConfigManager().isChannelAccessible(channel)
                && getMod().getDiscordManager().isChannelAccessible(user, channel);
    }

    /**
     * Checks if user the Discord channel is visible (a user can choose to hide the
     * channel).
     * 
     * @param channel
     * @return
     */
    public boolean isChannelVisible(MessageChannel channel) {
        try {
            UserData userData = this.getUserData().get();

            if (userData.getVerifyStatus() != VerifyStatus.BOTH) {
                throw new Exception("Account not verified");
            }

            if (!isChannelAccessible(channel)) {
                throw new Exception("Does not have channel access");
            }

            if (userData.getHiddenChannels().contains(UserData.ANY_CHANNEL)) {
                throw new Exception("All channels set inivisible");
            }

            if (userData.getHiddenChannels().contains(channel.getName())) {
                throw new Exception("Channel not set visibile");
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public List<TextChannel> getAccessibleChannels() {
        try {
            return getMod().getDiscordManager().getChannels().get().stream()
                    .filter(channel -> this.isChannelAccessible(channel)).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<TextChannel>();
        }
    }

    public List<TextChannel> getVisibleChannels() {
        try {
            return getMod().getDiscordManager().getChannels().get().stream()
                    .filter(channel -> this.isChannelVisible(channel)).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<TextChannel>();
        }
    }

    public List<TextChannel> getInvisibleChannels() {
        try {
            return getMod().getDiscordManager().getChannels().get().stream()
                    .filter(channel -> !this.isChannelVisible(channel)).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<TextChannel>();
        }
    }

    public void sendDiscordMessage(ITextComponent text) {
        this.user.openPrivateChannel().flatMap(channel -> channel.sendMessage(text.toString())).queue();
    }

    public void sendMCMessage(ITextComponent text) {
        this.playerEntity.sendStatusMessage(text, false);
    }

    public void sendMCMessage(ChatMessage chatMessage) {
        TextChannel textChannel = chatMessage.getTextChannel();

        if (chatMessage.isAuthor(this) || isChannelVisible(textChannel)) {
            this.sendMCMessage(chatMessage.getMCText(playerEntity));
        }
    }

    public String getDiscordAsMention() {
        return user.getAsMention();
    }

    public String getDiscordName() {
        return user.getName();
    }

    public String getMCName() {
        return playerEntity.getName().getString();
    }

    public String getDiscordUUID() {
        return user.getId();
    }

    public String getMCUUID() {
        return playerEntity.getUniqueID().toString();
    }

    public User getUser() {
        return this.user;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ModUser)) {
            return false;
        }

        ModUser user = (ModUser) obj;

        return this.getMCUUID().equals(user.getMCUUID());
    }
}
