package ca.concordia.mccord.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import ca.concordia.mccord.Config;
import ca.concordia.mccord.chat.ChatMessage;
import ca.concordia.mccord.discord.DiscordManager;
import ca.concordia.mccord.utils.DataManager;
import ca.concordia.mccord.utils.UserData;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;

public class MCCordUser {
    private ServerPlayerEntity playerEntity;
    private User user;

    private MCCordUser(Optional<ServerPlayerEntity> playerEntity, Optional<User> user) throws Exception {
        if (playerEntity.isPresent() && user.isPresent()) {
            this.playerEntity = playerEntity.get();
            this.user = user.get();
        } else if (playerEntity.isPresent()) {
            this.playerEntity = playerEntity.get();
            this.user = UserManager.getDiscordUserFromPlayerEntity(playerEntity).get();
        } else if (user.isPresent()) {
            this.playerEntity = UserManager.getPlayerEntityFromDiscordUser(user).get();
            this.user = user.get();
        } else {
            throw new Exception("Constructing empty MCCordUser.");
        }
    }

    private static Optional<MCCordUser> fromOptional(Optional<ServerPlayerEntity> playerEntity, Optional<User> user) {
        try {
            return Optional.ofNullable(new MCCordUser(playerEntity, user));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<MCCordUser> fromDiscordUUID(String discordUUID) {
        return fromOptional(Optional.empty(), UserManager.fromDiscordUUID(discordUUID));
    }

    public static Optional<MCCordUser> fromDiscordTag(String tag) {
        return fromOptional(Optional.empty(), UserManager.fromDiscordTag(tag));
    }

    public static Optional<MCCordUser> fromDiscordTag(String username, String discriminator) {
        return fromOptional(Optional.empty(), UserManager.fromDiscordTag(username, discriminator));
    }

    public static Optional<MCCordUser> fromDiscordUser(User user) {
        return fromDiscordUser(Optional.ofNullable(user));
    }

    public static Optional<MCCordUser> fromDiscordUser(Optional<User> user) {
        return fromOptional(Optional.empty(), user);
    }

    public static Optional<MCCordUser> fromMCName(String name) {
        return fromOptional(UserManager.fromMCName(name), Optional.empty());
    }

    public static Optional<MCCordUser> fromMCUUID(String uuid) {
        return fromOptional(UserManager.fromMCUUID(uuid), Optional.empty());
    }

    public static Optional<MCCordUser> fromMCPlayerEntity(Optional<ServerPlayerEntity> playerEntity) {
        return fromOptional(playerEntity, Optional.empty());
    }

    public static Optional<MCCordUser> fromMCPlayerEntity(ServerPlayerEntity playerEntity) {
        return fromMCPlayerEntity(Optional.ofNullable(playerEntity));
    }

    private Optional<UserData> getUserData() {
        return DataManager.getUserData(playerEntity);
    }

    private void setUserData(Consumer<UserData> function) {
        DataManager.setUserData(this.getMCUUID(), function);
    }

    /**
     * @return
     */
    public String getCurrentChannel() {
        String channel = this.getUserData().get().currentChannel;

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
            userData.currentChannel = channelName;
        });
    }

    /**
     * @return
     */
    public void setChannelHidden(String channelName) {
        this.setUserData(userData -> {
            userData.hiddenChannels.add(channelName);
        });
    }

    public void setChannelVisible(String channelName) {
        this.setUserData(userData -> {
            userData.hiddenChannels.remove(channelName);
        });
    }

    /**
     * @return
     */
    public void setAllChannelVisible() {
        this.setUserData(userData -> {
            userData.hiddenChannels = new HashSet<>();
        });
    }

    /**
     * @return
     */
    public void setNoChannelVisible() {
        this.setUserData(userData -> {
            userData.hiddenChannels = new HashSet<>();

            userData.hiddenChannels.add(UserData.ANY_CHANNEL);
        });
    }

    /**
     * Checks if user has Discord permissions to server.
     * 
     * @param channel
     * @return
     */
    public boolean isChannelAccessible(MessageChannel channel) {
        return Config.isChannelAccessible(channel) && DiscordManager.isChannelAccessible(user, channel);
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

            if (userData.hiddenChannels.contains(UserData.ANY_CHANNEL)) {
                throw new Exception("No visible channel");
            }

            if (userData.hiddenChannels.contains(channel.getName())) {
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
            return DiscordManager.getChannels().get().stream().filter(channel -> this.isChannelAccessible(channel))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<TextChannel>();
        }
    }

    public List<TextChannel> getVisibleChannels() {
        try {
            return DiscordManager.getChannels().get().stream().filter(channel -> this.isChannelVisible(channel))
                    .collect(Collectors.toList());
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

    public String getMCUUID() {
        return playerEntity.getUniqueID().toString();
    }
}
