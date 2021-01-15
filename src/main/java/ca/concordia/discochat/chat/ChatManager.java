package ca.concordia.discochat.chat;

import java.util.Optional;

import javax.naming.AuthenticationException;

import ca.concordia.discochat.chat.text.DiscordTextComponent;
import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.utils.AbstractManager;
import ca.concordia.discochat.utils.IMod;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.entity.player.ServerPlayerEntity;

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
        ModUser user = ModUser.fromDiscordUser(getMod(), message.getAuthor()).get();

        TextChannel channel = message.getTextChannel();

        DiscordTextComponent content = new DiscordTextComponent(getMod(), message.getContentRaw());

        broadcastMC(new ChatMessage(getMod(), user, channel, content));
    }

    public void broadcastMC(ChatMessage chatMessage) throws Exception {
        for (ServerPlayerEntity playerEntity : getMod().getServerManager().getServer().get().getPlayerList()
                .getPlayers()) {
            try {
                ModUser.fromMCPlayerEntity(getMod(), playerEntity).get().sendMCMessage(chatMessage);
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
        ModUser user = ModUser.fromMCPlayerEntity(getMod(), playerEntity).orElseThrow(AuthenticationException::new);

        broadcastAll(user, channelName, message);
    }

    public void broadcastAll(ModUser user, String channelName, String message) throws Exception {
        TextChannel textChannel = getMod().getDiscordManager().getChannelByName(channelName).get();

        ChatMessage chatMessage = new ChatMessage(getMod(), user, textChannel,
                new DiscordTextComponent(getMod(), message));

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
        ModUser user = ModUser.fromMCPlayerEntity(getMod(), playerEntity).orElseThrow(AuthenticationException::new);

        TextChannel textChannel = getMod().getDiscordManager().getChannelByName(channelName).get();

        ChatMessage chatMessage = new ChatMessage(getMod(), user, textChannel,
                new DiscordTextComponent(getMod(), message));

        broadcastDiscord(chatMessage);
    }

    public void broadcastDiscord(ChatMessage chatMessage) throws Exception {
        TextChannel textChannel = chatMessage.getTextChannel();

        if (chatMessage.getUser().isChannelVisible(textChannel)) {
            textChannel.sendMessage(chatMessage.getDiscordText()).queue();
        }
    }

    public void notifyDiscord(String message) {
        try {
            TextChannel textChannel = getMod().getDiscordManager()
                    .getChannelByName(getMod().getConfigManager().getNotificationChannel()).get();

            textChannel.sendMessage(message).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
