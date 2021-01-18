package ca.concordia.discochat.chat.text;

import java.util.Optional;

import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.utils.IMod;
import ca.concordia.discochat.utils.IModProvider;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class UserTextComponent extends TextComponent implements IModProvider {
    private IMod mod;

    private ModUser user;

    public UserTextComponent(IMod mod, String uuid, TextChannel textChannel) {
        this.mod = mod;

        StringTextComponent stringText;

        try {
            this.user = ModUser.fromDiscordUUID(getMod(), uuid).get();

            stringText = new StringTextComponent(user.getMCName());

            stringText.setStyle(Style.EMPTY
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new StringTextComponent("Mention @" + user.getDiscordName())))
                    .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, user.getDiscordAsMention()))
                    .setColor(net.minecraft.util.text.Color.fromInt(getUserHexColor(textChannel, user))));
        } catch (Exception e) {
            this.user = null;

            try {
                User user = getMod().getDiscordManager().getUserFromUUID(uuid).get();

                stringText = new StringTextComponent("@" + user.getName());

                stringText.setStyle(Style.EMPTY
                        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new StringTextComponent("Mention @" + user.getName())))
                        .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, user.getAsMention()))
                        .setColor(net.minecraft.util.text.Color.fromInt(getUserHexColor(textChannel, user))));
            } catch (Exception e2) {
                stringText = new StringTextComponent("<@" + uuid + ">");

                stringText.setStyle(Style.EMPTY.setColor(getMod().getConfigManager().getMentionColor()));
            }
        }

        siblings.add(stringText);
    }

    public UserTextComponent(IMod mod, String uuid) {
        this.mod = mod;

        StringTextComponent stringText;

        try {
            this.user = ModUser.fromDiscordUUID(getMod(), uuid).get();

            stringText = new StringTextComponent("@" + user.getMCName());

            stringText.setStyle(Style.EMPTY
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new StringTextComponent("Mention @" + user.getDiscordName())))
                    .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, user.getDiscordAsMention()))
                    .setColor(getMod().getConfigManager().getMentionColor()));
        } catch (Exception e) {
            this.user = null;

            try {
                User user = getMod().getDiscordManager().getUserFromUUID(uuid).get();

                stringText = new StringTextComponent("@" + user.getName());

                stringText.setStyle(Style.EMPTY
                        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new StringTextComponent("Mention @" + user.getName())))
                        .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, user.getAsMention()))
                        .setColor(getMod().getConfigManager().getMentionColor()));
            } catch (Exception e2) {
                stringText = new StringTextComponent("<@" + uuid + ">");

                stringText.setStyle(Style.EMPTY.setColor(getMod().getConfigManager().getMentionColor()));
            }
        }

        siblings.add(stringText);
    }

    public Optional<ModUser> getUser() {
        return Optional.ofNullable(user);
    }

    @Override
    public IMod getMod() {
        return mod;
    }

    @Override
    public TextComponent copyRaw() {
        // TODO Auto-generated method stub
        return null;
    }

    private int getUserHexColor(TextChannel textChannel, ModUser user) {
        return getUserHexColor(textChannel, user.getUser());
    }

    private int getUserHexColor(TextChannel textChannel, User user) {
        java.awt.Color color = textChannel.getGuild().getMemberById(user.getId()).getColor();

        if (color == null) {
            return java.awt.Color.WHITE.getRGB();
        }

        return color.getRGB();
    }
}
