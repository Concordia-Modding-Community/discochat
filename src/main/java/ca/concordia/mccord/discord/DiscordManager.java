package ca.concordia.mccord.discord;

import java.util.List;

import ca.concordia.mccord.Config;
import ca.concordia.mccord.MCCord;
import ca.concordia.mccord.Resources;
import ca.concordia.mccord.chat.ChatManager;
import ca.concordia.mccord.discord.commands.Command;
import ca.concordia.mccord.discord.commands.DiscordCommands;
import ca.concordia.mccord.utils.StringUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * A wrapper around JDA.
 */
@Mod.EventBusSubscriber(modid = Resources.MOD_ID, bus = Bus.FORGE)
public class DiscordManager extends ListenerAdapter {
    private static JDA jda;

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        DiscordManager.connect(Config.DISCORD_API_KEY.get());
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        DiscordManager.disconnect();
    }

    /**
     * Entrypoint for Discord connection. Checks if token is valid. This method is
     * automatically called by ServerEvents.
     * 
     * @param token The Discord API Token.
     */
    public static boolean connect(String token) {
        if (isConnected() || token.isBlank()) {
            return false;
        }

        try {
            jda = JDABuilder.createDefault(token).build();

            jda.addEventListener(new DiscordManager());

            jda.awaitReady();
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * Checks if there is an up and running Discord connection.
     * 
     * @return Discord connection active.
     */
    public static boolean isConnected() {
        return jda != null;
    }

    /**
     * Disconnects Discord connection if already connected. This method is
     * automatically called by ServerEvents.
     */
    public static void disconnect() {
        if (!isConnected()) {
            return;
        }

        jda.shutdown();

        jda = null;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        final Message message = event.getMessage();

        if (Command.isCommand(message.getContentRaw())) {
            handleCommand(message);
        } else {
            handleMessage(message);
        }
    }

    /**
     * Handles a command message to the Discord bot.
     * 
     * @param message Message to handle.
     */
    private void handleCommand(Message message) {
        List<String> tokens = StringUtils.tokenize(message.getContentRaw());

        Command command = DiscordCommands.get(tokens);

        if (command == null) {
            message.getChannel().sendMessage(String.format("Unknown command %s.", tokens.get(0))).queue();

            return;
        }

        command.execute(message);
    }

    /**
     * Handles any message that Discord bot has access to.
     * 
     * @param message Message to handle.
     */
    private void handleMessage(Message message) {
        ChatManager.broadcastMC(message);
    }

    /**
     * Gets list of Discord channels.
     */
    public static List<TextChannel> getChannels() {
        if (!isConnected()) {
            return null;
        }

        return jda.getTextChannels();
    }

    /**
     * Gets a Discord channel by name.
     */
    public static TextChannel getChannelByName(String name) {
        if (!isConnected()) {
            return null;
        }

        List<TextChannel> channels = jda.getTextChannelsByName(name, true);

        if (channels == null || channels.size() == 0) {
            return null;
        }

        return channels.get(0);
    }

    public static User getUserFromUUID(String uuid) {
        if (!isConnected()) {
            return null;
        }

        return jda.retrieveUserById(uuid).complete();
    }

    public static User getUserByTag(String tag) {
        if (!isConnected()) {
            return null;
        }

        return jda.getUserByTag(tag);
    }

    public static User getUserByTag(String username, String discriminator) {
        if(!isConnected()) {
            return null;
        }

        return jda.getUserByTag(username, discriminator);
    }

    public static boolean hasAccess(User user, MessageChannel messageChannel) {
        if (!isConnected()) {
            return false;
        }

        if(user == null || user.isBot()) {
            return false;
        }

        if(messageChannel == null || !(messageChannel instanceof TextChannel)) {
            return false;
        }

        TextChannel textChannel = (TextChannel) messageChannel;

        Member member = textChannel.getGuild().getMember(user);

        if (member == null) {
            MCCord.LOGGER.error("Unable to find member.");

            return false;
        }

        return member.hasAccess(textChannel);
    }
}
