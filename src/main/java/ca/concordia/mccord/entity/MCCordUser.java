package ca.concordia.mccord.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import ca.concordia.mccord.Config;
import ca.concordia.mccord.chat.ChatMessage;
import ca.concordia.mccord.data.UserData;
import ca.concordia.mccord.utils.IMod;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;

public class MCCordUser {
    private ServerPlayerEntity playerEntity;
    private User user;
    private IMod mod;

    private MCCordUser(IMod mod, Optional<ServerPlayerEntity> playerEntity, Optional<User> user) throws Exception {
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
            throw new Exception("Constructing empty MCCordUser.");
        }
    }

    protected IMod getMod() {
        return this.mod;
    }

    public MCCordUser(ServerPlayerEntity playerEntity, User user) {
        this.playerEntity = playerEntity;
        this.user = user;
    }

    private static Optional<MCCordUser> fromOptional(IMod mod, Optional<ServerPlayerEntity> playerEntity,
            Optional<User> user) {
        try {
            return Optional.ofNullable(new MCCordUser(mod, playerEntity, user));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<MCCordUser> fromDiscordUUID(IMod mod, String discordUUID) {
        return fromOptional(mod, Optional.empty(), mod.getUserManager().fromDiscordUUID(discordUUID));
    }

    public static Optional<MCCordUser> fromDiscordTag(IMod mod, String tag) {
        return fromOptional(mod, Optional.empty(), mod.getUserManager().fromDiscordTag(tag));
    }

    public static Optional<MCCordUser> fromDiscordTag(IMod mod, String username, String discriminator) {
        return fromOptional(mod, Optional.empty(), mod.getUserManager().fromDiscordTag(username, discriminator));
    }

    public static Optional<MCCordUser> fromDiscordUser(IMod mod, User user) {
        return fromDiscordUser(mod, Optional.ofNullable(user));
    }

    public static Optional<MCCordUser> fromDiscordUser(IMod mod, Optional<User> user) {
        return fromOptional(mod, Optional.empty(), user);
    }

    public static Optional<MCCordUser> fromMCName(IMod mod, String name) {
        return fromOptional(mod, mod.getUserManager().fromMCName(name), Optional.empty());
    }

    public static Optional<MCCordUser> fromMCUUID(IMod mod, String uuid) {
        return fromOptional(mod, mod.getUserManager().fromMCUUID(uuid), Optional.empty());
    }

    public static Optional<MCCordUser> fromMCPlayerEntity(IMod mod, Optional<ServerPlayerEntity> playerEntity) {
        return fromOptional(mod, playerEntity, Optional.empty());
    }

    public static Optional<MCCordUser> fromMCPlayerEntity(IMod mod, ServerPlayerEntity playerEntity) {
        return fromMCPlayerEntity(mod, Optional.ofNullable(playerEntity));
    }

    private Optional<UserData> getUserData() {
        return getMod().getDataManager().getUserData(playerEntity);
    }

    private void setUserData(Consumer<UserData> function) {
        getMod().getDataManager().setUserData(this.getMCUUID(), function);
    }

    /**
     * @return
     */
    public String getCurrentChannel() {
        String channel = this.getUserData().get().getCurrentChannel();

        if (channel.isBlank()) {
            return Config.DEFAULT_CHANNEL.get();
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
        return Config.isChannelAccessible(channel) && getMod().getDiscordManager().isChannelAccessible(user, channel);
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

            if (!isChannelAccessible(channel)) {
                throw new Exception("Does not have channel access");
            }

            if (userData.getHiddenChannels().contains(UserData.ANY_CHANNEL)) {
                throw new Exception("No visible channel");
            }

            if (userData.getHiddenChannels().contains(channel.getName())) {
                throw new Exception("Channel not set visibile");
            }
        } catch (Exception e) {
            e.printStackTrace();

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

    public void sendDiscordMessage(ITextComponent text) {
        this.user.openPrivateChannel().flatMap(channel -> channel.sendMessage(text.toString())).queue();
    }

    public void sendMCMessage(ITextComponent text) {
        this.playerEntity.sendStatusMessage(text, false);
    }

    public void sendMCMessage(ChatMessage chatMessage) {
        Optional<TextChannel> textChannel = chatMessage.getTextChannel();

        if (textChannel.isPresent() && isChannelVisible(textChannel.get())) {
            this.sendMCMessage(chatMessage.getMCText());
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
}
