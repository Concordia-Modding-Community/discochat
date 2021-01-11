package ca.concordia.b4dis;

import java.util.Optional;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * Discord command source.
 */
public class CommandSourceDiscord {
    /**
     * Discord message reference.
     */
    private Message message;

    public CommandSourceDiscord(Message message) {
        this.message = message;
    }

    /**
     * Checks if the author of Discord message has the Discord role.
     * 
     * @param roleName Discord role name.
     * @return Has role.
     */
    public boolean hasRole(String roleName) {
        try {
            return getChannel().get().getGuild().getMember(getUser()).getRoles().stream()
                    .anyMatch(role -> role.getName().equals(roleName));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets Discord message text channel.
     * 
     * @return Text channel or empty if channel is not a text channel.
     */
    public Optional<TextChannel> getChannel() {
        try {
            return Optional.of((TextChannel) message.getChannel());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Gets Discord message author.
     * 
     * @return Message author.
     */
    public User getUser() {
        return message.getAuthor();
    }
}
