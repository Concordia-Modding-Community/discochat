package ca.concordia.mccord.chat;

import java.util.Optional;

import javax.naming.AuthenticationException;

import ca.concordia.mccord.discord.DiscordManager;
import ca.concordia.mccord.entity.MCCordUser;
import ca.concordia.mccord.utils.ServerManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ChatManager {
    /**
     * Broadcasts a Discord message into Minecraft. This respects the Discord
     * channel permissions.
     * 
     * @param playerName
     * @param messageChannel
     * @param message
     */
    public static void broadcastMC(Message message) throws Exception {
        MCCordUser mcCordUser = MCCordUser.fromDiscordUser(message.getAuthor()).get();

        MessageChannel channel = message.getChannel();

        ITextComponent content = new StringTextComponent(message.getContentDisplay());

        broadcastMC(new ChatMessage(mcCordUser, channel, content));
    }

    public static void broadcastMC(ChatMessage chatMessage) throws Exception {
        for (ServerPlayerEntity playerEntity : ServerManager.getServer().get().getPlayerList().getPlayers()) {
            try {
                MCCordUser.fromMCPlayerEntity(playerEntity).get().sendMCMessage(chatMessage);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void broadcastAll(ServerPlayerEntity playerEntity, String channelName, String message) throws Exception {
        broadcastAll(Optional.ofNullable(playerEntity), channelName, message);
    }

    public static void broadcastAll(Optional<ServerPlayerEntity> playerEntity, String channelName, String message)
            throws Exception {
        MCCordUser mcCordUser = MCCordUser.fromMCPlayerEntity(playerEntity).orElseThrow(AuthenticationException::new);

        broadcastAll(mcCordUser, channelName, message);
    }

    public static void broadcastAll(MCCordUser mcCordUser, String channelName, String message) throws Exception {
        TextChannel textChannel = DiscordManager.getChannelByName(channelName).get();

        ChatMessage chatMessage = new ChatMessage(mcCordUser, textChannel, new StringTextComponent(message));

        broadcastAll(chatMessage);
    }

    public static void broadcastAll(ChatMessage chatMessage) throws Exception {

        broadcastMC(chatMessage);

        broadcastDiscord(chatMessage);
    }

    public static void broadcastDiscord(ServerPlayerEntity playerEntity, String channelName, String message) throws Exception {
        broadcastDiscord(Optional.ofNullable(playerEntity), channelName, message);
    }

    public static void broadcastDiscord(Optional<ServerPlayerEntity> playerEntity, String channelName, String message)
            throws Exception {
        MCCordUser mcCordUser = MCCordUser.fromMCPlayerEntity(playerEntity).orElseThrow(AuthenticationException::new);

        TextChannel textChannel = DiscordManager.getChannelByName(channelName).get();

        ChatMessage chatMessage = new ChatMessage(mcCordUser, textChannel, new StringTextComponent(message));

        broadcastDiscord(chatMessage);
    }

    public static void broadcastDiscord(ChatMessage chatMessage) throws Exception {
        TextChannel textChannel = chatMessage.getTextChannel().get();

        if(chatMessage.getMCCordUser().isChannelVisible(textChannel)) {
            textChannel.sendMessage(chatMessage.getDiscordText()).queue();
        }
    }
}
