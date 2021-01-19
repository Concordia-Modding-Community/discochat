package ca.concordia.discochat.chat.text;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.utils.IMod;
import ca.concordia.discochat.utils.IModProvider;
import ca.concordia.jddown.fragment.AtMentionFragment;
import ca.concordia.jddown.fragment.EmojiFragment;
import ca.concordia.jddown.fragment.MentionFragment;
import ca.concordia.jddown.fragment.TextFormattingFragment;
import ca.concordia.jddown.fragment.TextFragment;
import ca.concordia.jddown.fragment.URLFragment;
import ca.concordia.jddown.parser.DiscordTextParser;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.Style;

public class DiscordTextComponent implements IModProvider {
    private IMod mod;
    private String discordText;
    private StringTextComponent stringText;
    private Set<String> mentionedDiscordUUID;
    private Set<String> mentionedMCUUID;

    public DiscordTextComponent(IMod mod, String text) {
        this.mod = mod;

        this.discordText = text + "";

        this.mentionedDiscordUUID = new HashSet<String>();

        this.mentionedMCUUID = new HashSet<String>();

        parse(text);
    }

    private void parse(String text) {
        this.stringText = new StringTextComponent("");

        new DiscordTextParser(text).stream(fragment -> {
            if (fragment instanceof TextFragment) {
                this.stringText.append(new StringTextComponent(fragment.getText()));
            } else if (fragment instanceof TextFormattingFragment) {
                StringTextComponent stringText = new StringTextComponent(fragment.getText());

                TextFormattingFragment formattingFragment = (TextFormattingFragment) fragment;

                TextFormattingFragment.Style style = formattingFragment.getStyle();

                stringText.setStyle(Style.EMPTY.setBold(style.isBold()).setItalic(style.isItalic())
                        .setStrikethrough(style.isStrikethrough()).setUnderlined(style.isUnderline()));

                this.stringText.append(stringText);
            } else if (fragment instanceof URLFragment) {
                this.stringText.append(URLTextComponent.from(getMod(), fragment.getText()));
            } else if (fragment instanceof MentionFragment) {
                MentionFragment mention = (MentionFragment) fragment;

                String uuid = mention.getUUID();

                switch (mention.getType()) {
                    case CHANNEL:
                        this.stringText.append(ChannelTextComponent.from(getMod(), uuid));

                        break;
                    case USER:
                        StringTextComponent userText = UserTextComponent.from(getMod(), uuid);

                        this.stringText.append(userText);

                        mentionedDiscordUUID.add(uuid);

                        Optional<ModUser> oUser = ModUser.fromDiscordUUID(getMod(), uuid);

                        if(oUser.isPresent()) {
                            mentionedMCUUID.add(oUser.get().getMCUUID());
                        }

                        break;
                    case ROLE:
                        this.stringText.append(RoleTextComponent.from(getMod(), uuid));

                        break;
                }
            } else if (fragment instanceof AtMentionFragment) {
                StringTextComponent stringText = new StringTextComponent("@" + fragment.getText());

                Style style = Style.EMPTY;

                style = style.setColor(Color.fromTextFormatting(TextFormatting.BLUE));

                stringText.setStyle(style);

                this.stringText.append(stringText);
            } else if (fragment instanceof EmojiFragment) {
                EmojiFragment emojiFragment = (EmojiFragment) fragment;

                String emoji;

                switch (emojiFragment.getText()) {
                    case "skull":
                        emoji = "☠";
                        break;
                    case "peace":
                        emoji = "☮";
                        break;
                    default:
                        emoji = "?";
                }

                this.stringText.append(new StringTextComponent(emoji));
            }
        });
    }

    public Set<String> getMentionedMCUUID() {
        return mentionedMCUUID;
    }

    public Set<String> getMentionedDiscordUUID() {
        return mentionedDiscordUUID;
    }

    public String getDiscordString() {
        return discordText;
    }

    public StringTextComponent getMCString() {
        return stringText;
    }

    public String toString() {
        return "DiscordTextComponent{}";
    }

    @Override
    public IMod getMod() {
        return mod;
    }
}
