package ca.concordia.mccord.discord;

import java.util.List;
import java.util.Optional;

import ca.concordia.mccord.Config;
import ca.concordia.mccord.MCCord;
import ca.concordia.mccord.Resources;
import ca.concordia.mccord.chat.ChatManager;
import ca.concordia.mccord.discord.commands.Command;
import ca.concordia.mccord.discord.commands.DiscordCommands;
import ca.concordia.mccord.utils.StringUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * A wrapper around JDA.
 */
@Mod.EventBusSubscriber(modid = Resources.MOD_ID, bus = Bus.FORGE)
public class DiscordManager extends ListenerAdapter {
    private static Optional<JDA> jda = Optional.empty();

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
        try {
            if (isConnected()) {
                throw new Exception("JDA already connected.");
            }

            if (token.isBlank()) {
                throw new Exception("Invalid token.");
            }

            JDA tJDA = JDABuilder.createDefault(token).build();

            tJDA.addEventListener(new DiscordManager());

            tJDA.awaitReady();

            jda = Optional.ofNullable(tJDA);
        } catch (Exception e) {
            MCCord.LOGGER.error(e.getMessage());

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
        return jda.isPresent();
    }

    /**
     * Disconnects Discord connection if already connected. This method is
     * automatically called by ServerEvents.
     */
    public static void disconnect() {
        try {
            jda.get().shutdown();

            jda = Optional.empty();
        } catch (Exception e) {}
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
        try {
            List<String> tokens = StringUtils.tokenize(message.getContentRaw());

            Command command = DiscordCommands.getCommand(tokens).orElseThrow(
                    () -> new CommandException(new StringTextComponent("Unknown command " + tokens.get(0))));

            command.execute(message);
        } catch (CommandException e) {
            message.getChannel().sendMessage(e.getMessage()).queue();
        }
    }

    /**
     * Handles any message that Discord bot has access to.
     * 
     * @param message Message to handle.
     */
    private void handleMessage(Message message) {
        try {
            ChatManager.broadcastMC(message);
        } catch(Exception e) {
            message.getChannel().sendMessage("Unable to send message to Minecraft.").queue();
        }
    }

    /**
     * Gets list of Discord channels.
     */
    public static Optional<List<TextChannel>> getChannels() {
        try {
            return Optional.ofNullable(jda.get().getTextChannels());
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a Discord channel by name.
     * 
     * TODO: Make sure the channels is singular?
     */
    public static Optional<TextChannel> getChannelByName(String name) {
        try {
            List<TextChannel> channels = jda.get().getTextChannelsByName(name, true);

            return Optional.ofNullable(channels.get(0));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<User> getUserFromUUID(String uuid) {
        try {
            return Optional.ofNullable(jda.get().retrieveUserById(uuid).complete());
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<User> getUserByTag(String tag) {
        try {
            return Optional.ofNullable(jda.get().getUserByTag(tag));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<User> getUserByTag(String username, String discriminator) {
        try {
            return Optional.ofNullable(jda.get().getUserByTag(username, discriminator));
        } catch(Exception e) {
            return Optional.empty();
        }
    }
}
