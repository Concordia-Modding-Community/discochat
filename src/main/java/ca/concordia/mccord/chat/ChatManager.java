package ca.concordia.mccord.chat;

import ca.concordia.mccord.discord.DiscordManager;
import ca.concordia.mccord.entity.UserManager;
import ca.concordia.mccord.world.ServerManager;
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

    public static void messageMC(PlayerEntity playerEntity, String authorName, MessageChannel channel, String message) {
        User user = UserManager.getDiscordUser(playerEntity);

        if (!DiscordManager.hasAccess(user, channel)) {
            return;
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
    public static void broadcastMC(Message message) {
        User author = message.getAuthor();

        ServerPlayerEntity playerEntity = UserManager.getMCPlayer(author.getId());

        String authorName;

        if (playerEntity == null) {
            authorName = author.getName();
        } else {
            authorName = playerEntity.getName().getString();
        }

        MessageChannel channel = message.getChannel();
        String content = message.getContentDisplay();

        for (PlayerEntity player : ServerManager.getServer().getPlayerList().getPlayers()) {
            messageMC(player, authorName, channel, content);
        }
    }

    public static void broadcastMC(ITextComponent message) {
        for (PlayerEntity player : ServerManager.getServer().getPlayerList().getPlayers()) {
            player.sendStatusMessage(message, false);
        }
    }

    public static boolean broadcastAll(PlayerEntity playerEntity, String channelName, String message) {
        TextChannel textChannel = DiscordManager.getChannelByName(channelName);
        
        if (playerEntity == null || textChannel == null) {
            return false;
        }

        User user = UserManager.getDiscordUser(playerEntity.getUniqueID().toString());

        if (!DiscordManager.hasAccess(user, textChannel)) {
            return false;
        }

        ITextComponent mcMessage = generateMCMessage(playerEntity.getName().getString(), channelName, message);

        for (PlayerEntity player : ServerManager.getServer().getPlayerList().getPlayers()) {
            player.sendStatusMessage(mcMessage, false);
        }

        textChannel.sendMessage(generateDiscordMessage(user.getAsMention(), message)).queue();

        return true;
    }

    public static boolean discordChannelMessage(PlayerEntity playerEntity, String channelName, String message) {
        TextChannel textChannel = DiscordManager.getChannelByName(channelName);
        
        if (playerEntity == null || textChannel == null) {
            return false;
        }

        User user = UserManager.getDiscordUser(playerEntity.getUniqueID().toString());

        if (!DiscordManager.hasAccess(user, textChannel)) {
            return false;
        }

        textChannel.sendMessage(generateDiscordMessage(user.getAsMention(), message)).queue();

        messageMC(playerEntity, playerEntity.getName().getString(), textChannel, message);

        return true;
    }
}
