package ca.concordia.discochat.chat.text;

import java.util.HashSet;
import java.util.Set;

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
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.Style;

public class DiscordTextComponent extends TextComponent implements IModProvider {
    private IMod mod;
    private String discordText;
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
        new DiscordTextParser(text).stream(fragment -> {
            if (fragment instanceof TextFragment) {
                siblings.add(new StringTextComponent(fragment.getText()));
            } else if (fragment instanceof TextFormattingFragment) {
                StringTextComponent stringText = new StringTextComponent(fragment.getText());

                TextFormattingFragment formattingFragment = (TextFormattingFragment) fragment;

                TextFormattingFragment.Style style = formattingFragment.getStyle();

                stringText.setStyle(Style.EMPTY.setBold(style.isBold()).setItalic(style.isItalic())
                        .setStrikethrough(style.isStrikethrough()).setUnderlined(style.isUnderline()));

                siblings.add(stringText);
            } else if (fragment instanceof URLFragment) {
                siblings.add(new URLTextComponent(getMod(), fragment.getText()));
            } else if (fragment instanceof MentionFragment) {
                MentionFragment mention = (MentionFragment) fragment;

                String uuid = mention.getUUID();

                switch (mention.getType()) {
                    case CHANNEL:
                        siblings.add(new ChannelTextComponent(getMod(), uuid));

                        break;
                    case USER:
                        UserTextComponent userText = new UserTextComponent(getMod(), uuid);

                        siblings.add(userText);

                        mentionedDiscordUUID.add(uuid);

                        mentionedMCUUID.add(userText.getUser().getMCUUID());

                        break;
                    case ROLE:
                        siblings.add(new RoleTextComponent(getMod(), uuid));

                        break;
                }
            } else if (fragment instanceof AtMentionFragment) {
                StringTextComponent stringText = new StringTextComponent("@" + fragment.getText());

                Style style = Style.EMPTY;

                style = style.setColor(Color.fromTextFormatting(TextFormatting.BLUE));

                stringText.setStyle(style);

                siblings.add(stringText);
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

                siblings.add(new StringTextComponent(emoji));
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

    @Override
    public TextComponent copyRaw() {
        // TODO
        return null;
    }

    @Override
    public IMod getMod() {
        return mod;
    }
}
