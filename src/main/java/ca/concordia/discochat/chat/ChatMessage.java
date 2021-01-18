package ca.concordia.discochat.chat;

import java.util.Optional;

import ca.concordia.discochat.chat.text.ChannelTextComponent;
import ca.concordia.discochat.chat.text.DiscordTextComponent;
import ca.concordia.discochat.chat.text.FormatTextComponent;
import ca.concordia.discochat.chat.text.UserTextComponent;
import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.utils.IMod;
import ca.concordia.discochat.utils.IModProvider;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class ChatMessage implements IModProvider {
    private IMod mod;
    private ModUser modUser;
    private User user;
    private TextChannel textChannel;
    private DiscordTextComponent message;

    public ChatMessage(IMod mod, User user, TextChannel textChannel, DiscordTextComponent message) {
        this.mod = mod;
        this.modUser = null;
        this.textChannel = textChannel;
        this.message = message;
        this.user = user;
    }

    public ChatMessage(IMod mod, ModUser user, TextChannel textChannel, DiscordTextComponent message) {
        this.mod = mod;
        this.modUser = user;
        this.textChannel = textChannel;
        this.message = message;
        this.user = user.getUser();
    }

    @Override
    public IMod getMod() {
        return mod;
    }

    public String getDiscordText() {
        StringTextComponent discordMessage = new StringTextComponent(message.getDiscordString());

        StringTextComponent discordName = new StringTextComponent(user.getName());

        return new FormatTextComponent(getMod().getConfigManager().getDiscordTextFormat()).put("m", discordMessage)
                .put("p", discordName).build().getString();
    }

    public ITextComponent getMCText(PlayerEntity playerEntity) {
        UserTextComponent userText;

        try {
            userText = new UserTextComponent(mod, getModUser().get().getDiscordUUID(), textChannel);
        } catch(Exception e) {
            userText = new UserTextComponent(mod, user.getId(), textChannel);
        }

        Style style = Style.EMPTY;

        if (message.getMentionedMCUUID().contains(playerEntity.getUniqueID().toString())) {
            style = style.setColor(Color.fromTextFormatting(TextFormatting.YELLOW));
        }

        FormatTextComponent fullText = new FormatTextComponent(mod.getConfigManager().getMCTextFormat())
                .put("c", new ChannelTextComponent(mod, textChannel)).put("m", message).put("p", userText).build();

        fullText.setStyle(style);

        return fullText;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public Optional<ModUser> getModUser() {
        return Optional.ofNullable(this.modUser);
    }

    public boolean isAuthor(ModUser modUser) {
        if (this.modUser == null || modUser == null) {
            return false;
        }

        return this.modUser.equals(modUser);
    }
}
