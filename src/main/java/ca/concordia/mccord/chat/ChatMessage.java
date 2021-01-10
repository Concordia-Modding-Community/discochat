package ca.concordia.mccord.chat;

import java.util.Optional;

import ca.concordia.mccord.entity.MCCordUser;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class ChatMessage {
    private MCCordUser mcCordUser;
    private MessageChannel messageChannel;
    private ITextComponent message;

    public ChatMessage(MCCordUser mcCordUser, MessageChannel messageChannel, ITextComponent message) {
        this.mcCordUser = mcCordUser;
        this.messageChannel = messageChannel;
        this.message = message;
    }

    public String getDiscordText() {
        return String.format("%s: %s", mcCordUser.getDiscordAsMention(), message.getString());
    }

    public ITextComponent getMCText() {
        return new StringTextComponent(mcCordUser.getMCName() + " [" + TextFormatting.BLUE + "#"
                + messageChannel.getName() + TextFormatting.RESET + "]: ").append(message);
    }

    public Optional<TextChannel> getTextChannel() {
        if (!(messageChannel instanceof TextChannel)) {
            return Optional.empty();
        }

        return Optional.of((TextChannel) messageChannel);
    }

    public MCCordUser getMCCordUser() {
        return this.mcCordUser;
    }
}
