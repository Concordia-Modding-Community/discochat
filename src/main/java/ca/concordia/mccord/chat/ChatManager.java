package ca.concordia.mccord.chat;

import java.util.Optional;

import javax.naming.AuthenticationException;

import ca.concordia.mccord.entity.MCCordUser;
import ca.concordia.mccord.utils.AbstractManager;
import ca.concordia.mccord.utils.IMod;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ChatManager extends AbstractManager {
    public ChatManager(IMod mod) {
        super(mod);
    }

    /**
     * Broadcasts a Discord message into Minecraft. This respects the Discord
     * channel permissions.
     * 
     * @param playerName
     * @param messageChannel
     * @param message
     */
    public void broadcastMC(Message message) throws Exception {
        MCCordUser mcCordUser = MCCordUser.fromDiscordUser(getMod(), message.getAuthor()).get();

        MessageChannel channel = message.getChannel();

        ITextComponent content = new StringTextComponent(message.getContentDisplay());

        broadcastMC(new ChatMessage(mcCordUser, channel, content));
    }

    public void broadcastMC(ChatMessage chatMessage) throws Exception {
        for (ServerPlayerEntity playerEntity : getMod().getServerManager().getServer().get().getPlayerList()
                .getPlayers()) {
            try {
                MCCordUser.fromMCPlayerEntity(getMod(), playerEntity).get().sendMCMessage(chatMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastAll(ServerPlayerEntity playerEntity, String channelName, String message) throws Exception {
        broadcastAll(Optional.ofNullable(playerEntity), channelName, message);
    }

    public void broadcastAll(Optional<ServerPlayerEntity> playerEntity, String channelName, String message)
            throws Exception {
        MCCordUser mcCordUser = MCCordUser.fromMCPlayerEntity(getMod(), playerEntity)
                .orElseThrow(AuthenticationException::new);

        broadcastAll(mcCordUser, channelName, message);
    }

    public void broadcastAll(MCCordUser mcCordUser, String channelName, String message) throws Exception {
        TextChannel textChannel = getMod().getDiscordManager().getChannelByName(channelName).get();

        ChatMessage chatMessage = new ChatMessage(mcCordUser, textChannel, new StringTextComponent(message));

        broadcastAll(chatMessage);
    }

    public void broadcastAll(ChatMessage chatMessage) throws Exception {
        broadcastMC(chatMessage);

        broadcastDiscord(chatMessage);
    }

    public void broadcastDiscord(ServerPlayerEntity playerEntity, String channelName, String message) throws Exception {
        broadcastDiscord(Optional.ofNullable(playerEntity), channelName, message);
    }

    public void broadcastDiscord(Optional<ServerPlayerEntity> playerEntity, String channelName, String message)
            throws Exception {
        MCCordUser mcCordUser = MCCordUser.fromMCPlayerEntity(getMod(), playerEntity)
                .orElseThrow(AuthenticationException::new);

        TextChannel textChannel = getMod().getDiscordManager().getChannelByName(channelName).get();

        ChatMessage chatMessage = new ChatMessage(mcCordUser, textChannel, new StringTextComponent(message));

        broadcastDiscord(chatMessage);
    }

    public void broadcastDiscord(ChatMessage chatMessage) throws Exception {
        TextChannel textChannel = chatMessage.getTextChannel().get();

        if (chatMessage.getMCCordUser().isChannelVisible(textChannel)) {
            textChannel.sendMessage(chatMessage.getDiscordText()).queue();
        }
    }
}
