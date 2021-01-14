package ca.concordia.mccord.discord;

import java.util.List;
import java.util.Optional;

import ca.concordia.mccord.Config;
import ca.concordia.mccord.discord.commands.DiscordCommandManager;
import ca.concordia.mccord.utils.AbstractManager;
import ca.concordia.mccord.utils.IMod;
import ca.concordia.b4dis.DiscordBrigadier;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * A wrapper around JDA.
 */
public class DiscordManager extends AbstractManager {
    private Optional<JDA> jda = Optional.empty();
    private DiscordBrigadier discordBrigadier = new DiscordBrigadier(() -> Config.DISCORD_COMMAND_PREFIX.get(),
            message -> handleMessage(message));
    private DiscordCommandManager discordCommandManager;

    public DiscordManager(IMod mod) {
        super(mod);

        this.discordCommandManager = new DiscordCommandManager(mod);
    }

    public DiscordManager register() {
        connect(Config.DISCORD_API_KEY.get());

        discordCommandManager.register(discordBrigadier.getDispatcher());

        return this;
    }

    public void unregister() {
        disconnect();
    }

    /**
     * Entrypoint for Discord connection. Checks if token is valid. This method is
     * automatically called by ServerEvents.
     * 
     * @param token The Discord API Token.
     */
    public boolean connect(String token) {
        try {
            if (isConnected()) {
                throw new Exception("JDA already connected.");
            }

            if (token.isBlank()) {
                throw new Exception("Invalid token.");
            }

            JDA tJDA = JDABuilder.createDefault(token).build();

            tJDA.addEventListener(discordBrigadier);

            tJDA.awaitReady();

            jda = Optional.ofNullable(tJDA);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Checks if there is an up and running Discord connection.
     * 
     * @return Discord connection active.
     */
    public boolean isConnected() {
        return jda.isPresent();
    }

    /**
     * Disconnects Discord connection if already connected. This method is
     * automatically called by ServerEvents.
     */
    public void disconnect() {
        try {
            jda.get().shutdown();

            jda = Optional.empty();
        } catch (Exception e) {
        }
    }

    /**
     * Handles any message that Discord bot has access to.
     * 
     * @param message Message to handle.
     */
    private void handleMessage(Message message) {
        try {
            getMod().getChatManager().broadcastMC(message);
        } catch (Exception e) {
            message.getChannel().sendMessage("Unable to send message to MC.").queue();
        }
    }

    /**
     * Gets list of Discord channels.
     */
    public Optional<List<TextChannel>> getChannels() {
        try {
            return Optional.ofNullable(jda.get().getTextChannels());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Gets a Discord channel by name.
     * 
     * TODO: Make sure the channels is singular?
     */
    public Optional<TextChannel> getChannelByName(String name) {
        try {
            List<TextChannel> channels = jda.get().getTextChannelsByName(name, true);

            return Optional.ofNullable(channels.get(0));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> getUserFromUUID(String uuid) {
        try {
            return Optional.ofNullable(jda.get().retrieveUserById(uuid).complete());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> getUserByTag(String tag) {
        try {
            return Optional.ofNullable(jda.get().getUserByTag(tag));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> getUserByTag(String username, String discriminator) {
        try {
            return Optional.ofNullable(jda.get().getUserByTag(username, discriminator));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean isChannelAccessible(User user, MessageChannel channel) {
        try {
            TextChannel textChannel = (TextChannel) channel;

            return textChannel.getGuild().getMember(user).hasAccess(textChannel);
        } catch (Exception e) {
            return false;
        }
    }
}
