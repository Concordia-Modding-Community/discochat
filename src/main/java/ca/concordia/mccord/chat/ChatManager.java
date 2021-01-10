package ca.concordia.mccord.chat;

import java.util.Optional;

import javax.naming.AuthenticationException;

import ca.concordia.mccord.discord.DiscordManager;
import ca.concordia.mccord.entity.UserManager;
import ca.concordia.mccord.world.ServerManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class ChatManager {
    /**
     * Generates the Minecraft Discord message.
     * 
     * @param playerName
     * @param channelName
     * @param message
     * @return
     */
    private static ITextComponent generateMCMessage(String playerName, String channelName, String message) {
        return new StringTextComponent(
                playerName + " [" + TextFormatting.BLUE + "#" + channelName + TextFormatting.RESET + "]: " + message);
    }

    private static String generateDiscordMessage(String playerName, String message) {
        return String.format("%s: %s", playerName, message);
    }

    public static boolean hasAccess(User user, MessageChannel messageChannel) {
        return hasAccess(Optional.ofNullable(user), Optional.ofNullable(messageChannel));
    }

    public static boolean hasAccess(Optional<User> oUser, Optional<MessageChannel> oMessageChannel) {
        try {
            User user = oUser.get();

            TextChannel textChannel = (TextChannel) oMessageChannel.get();

            Member member = Optional.ofNullable(textChannel.getGuild().getMember(user)).get();

            return member.hasAccess(textChannel);
        } catch (Exception e) {
            return false;
        }
    }

    public static void messageMC(PlayerEntity playerEntity, String authorName, MessageChannel channel, String message)
            throws Exception {
        User user = UserManager.getDiscordUser(playerEntity).orElseThrow(AuthenticationException::new);

        if (!hasAccess(user, channel)) {
            throw new AuthenticationException();
        }

        String channelName = channel.getName();

        final ITextComponent text = generateMCMessage(authorName, channelName, message);

        playerEntity.sendStatusMessage(text, false);
    }

    /**
     * Broadcasts a Discord message into Minecraft. This respects the Discord
     * channel permissions.
     * 
     * @param playerName
     * @param messageChannel
     * @param message
     */
    public static void broadcastMC(Message message) throws Exception {
        User author = message.getAuthor();

        ServerPlayerEntity playerEntity = UserManager.getMCPlayer(author.getId()).orElseThrow(AuthenticationException::new);

        String authorName = playerEntity.getName().getString();

        MessageChannel channel = message.getChannel();

        String content = message.getContentDisplay();

        for (PlayerEntity player : ServerManager.getServer().get().getPlayerList().getPlayers()) {
            messageMC(player, authorName, channel, content);
        }
    }

    public static void broadcastMC(ITextComponent message) throws Exception {
        for (PlayerEntity player : ServerManager.getServer().get().getPlayerList().getPlayers()) {
            player.sendStatusMessage(message, false);
        }
    }

    public static void broadcastAll(PlayerEntity playerEntity, String channelName, String message) throws Exception {
        broadcastAll(Optional.ofNullable(playerEntity), channelName, message);
    }

    public static void broadcastAll(Optional<PlayerEntity> oPlayerEntity, String channelName, String message)
            throws Exception {
        PlayerEntity playerEntity = oPlayerEntity.get();

        TextChannel textChannel = DiscordManager.getChannelByName(channelName).get();

        User user = UserManager.getDiscordUser(playerEntity.getUniqueID().toString()).orElseThrow(AuthenticationException::new);

        if (!hasAccess(user, textChannel)) {
            throw new AuthenticationException();
        }

        ITextComponent mcMessage = generateMCMessage(playerEntity.getName().getString(), channelName, message);

        for (PlayerEntity player : ServerManager.getServer().get().getPlayerList().getPlayers()) {
            player.sendStatusMessage(mcMessage, false);
        }

        textChannel.sendMessage(generateDiscordMessage(user.getAsMention(), message)).queue();
    }

    public static void discordChannelMessage(PlayerEntity playerEntity, String channelName, String message)
            throws Exception {
        discordChannelMessage(Optional.ofNullable(playerEntity), channelName, message);
    }

    public static void discordChannelMessage(Optional<PlayerEntity> oPlayerEntity, String channelName, String message)
            throws Exception {
        PlayerEntity playerEntity = oPlayerEntity.get();
        TextChannel textChannel = DiscordManager.getChannelByName(channelName).get();

        User user = UserManager.getDiscordUser(playerEntity.getUniqueID().toString()).orElseThrow(AuthenticationException::new);

        if (!hasAccess(user, textChannel)) {
            throw new AuthenticationException();
        }

        textChannel.sendMessage(generateDiscordMessage(user.getAsMention(), message)).queue();

        messageMC(playerEntity, playerEntity.getName().getString(), textChannel, message);
    }
}
