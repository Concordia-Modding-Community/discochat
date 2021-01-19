package ca.concordia.discochat.chat.text;

import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.utils.IMod;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class UserTextComponent {
    public static StringTextComponent from(IMod mod, String uuid, TextChannel textChannel) {
        StringTextComponent stringText;

        try {
            ModUser user = ModUser.fromDiscordUUID(mod, uuid).get();

            stringText = new StringTextComponent(user.getMCName());

            stringText.setStyle(Style.EMPTY
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new StringTextComponent("Mention @" + user.getDiscordName())))
                    .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, user.getDiscordAsMention()))
                    .setColor(net.minecraft.util.text.Color.fromInt(getUserHexColor(textChannel, user))));
        } catch (Exception e) {
            try {
                User user = mod.getDiscordManager().getUserFromUUID(uuid).get();

                stringText = new StringTextComponent("@" + user.getName());

                stringText.setStyle(Style.EMPTY
                        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new StringTextComponent("Mention @" + user.getName())))
                        .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, user.getAsMention()))
                        .setColor(net.minecraft.util.text.Color.fromInt(getUserHexColor(textChannel, user))));
            } catch (Exception e2) {
                stringText = new StringTextComponent("<@" + uuid + ">");

                stringText.setStyle(Style.EMPTY.setColor(mod.getConfigManager().getMentionColor()));
            }
        }

        return stringText;
    }

    public static StringTextComponent from(IMod mod, String uuid) {
        StringTextComponent stringText;

        try {
            ModUser user = ModUser.fromDiscordUUID(mod, uuid).get();

            stringText = new StringTextComponent("@" + user.getMCName());

            stringText.setStyle(Style.EMPTY
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new StringTextComponent("Mention @" + user.getDiscordName())))
                    .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, user.getDiscordAsMention()))
                    .setColor(mod.getConfigManager().getMentionColor()));
        } catch (Exception e) {
            try {
                User user = mod.getDiscordManager().getUserFromUUID(uuid).get();

                stringText = new StringTextComponent("@" + user.getName());

                stringText.setStyle(Style.EMPTY
                        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new StringTextComponent("Mention @" + user.getName())))
                        .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, user.getAsMention()))
                        .setColor(mod.getConfigManager().getMentionColor()));
            } catch (Exception e2) {
                stringText = new StringTextComponent("<@" + uuid + ">");

                stringText.setStyle(Style.EMPTY.setColor(mod.getConfigManager().getMentionColor()));
            }
        }

        return stringText;
    }

    private static int getUserHexColor(TextChannel textChannel, ModUser user) {
        return getUserHexColor(textChannel, user.getUser());
    }

    private static int getUserHexColor(TextChannel textChannel, User user) {
        java.awt.Color color = textChannel.getGuild().getMemberById(user.getId()).getColor();

        if (color == null) {
            return java.awt.Color.WHITE.getRGB();
        }

        return color.getRGB();
    }
}
