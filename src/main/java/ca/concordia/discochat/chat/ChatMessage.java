package ca.concordia.discochat.chat;

import java.util.List;

import ca.concordia.discochat.chat.text.ChannelTextComponent;
import ca.concordia.discochat.chat.text.DiscordTextComponent;
import ca.concordia.discochat.chat.text.FormatTextComponent;
import ca.concordia.discochat.chat.text.UserTextComponent;
import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.utils.IMod;
import ca.concordia.discochat.utils.IModProvider;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class ChatMessage implements IModProvider {
    private IMod mod;
    private ModUser user;
    private TextChannel textChannel;
    private DiscordTextComponent message;

    public ChatMessage(IMod mod, ModUser user, TextChannel textChannel, DiscordTextComponent message) {
        this.mod = mod;
        this.user = user;
        this.textChannel = textChannel;
        this.message = message;
    }

    @Override
    public IMod getMod() {
        return mod;
    }

    public String getDiscordText() {
        return new FormatTextComponent(getMod().getConfigManager().getDiscordTextFormat(), new String[] { "m", "p" },
                new ITextComponent[] { new StringTextComponent(message.getDiscordString()),
                        new StringTextComponent(user.getDiscordName()) }).getString();
    }

    public ITextComponent getMCText(PlayerEntity playerEntity) {
        UserTextComponent userText = new UserTextComponent(mod, user.getDiscordUUID(), textChannel);

        Style style = Style.EMPTY;

        if (message.getMentionedMCUUID().contains(playerEntity.getUniqueID().toString())) {
            style = style.setColor(Color.fromTextFormatting(TextFormatting.YELLOW));
        }

        FormatTextComponent fullText = new FormatTextComponent(mod.getConfigManager().getMCTextFormat(),
                new String[] { "c", "m", "p" },
                new ITextComponent[] { new ChannelTextComponent(mod, textChannel), message, userText });

        fullText.setStyle(style);

        return fullText;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public ModUser getUser() {
        return this.user;
    }
}
