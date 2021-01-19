package ca.concordia.discochat.chat.text;

import ca.concordia.discochat.utils.IMod;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class ChannelTextComponent {
    public static StringTextComponent from(IMod mod, String uuid) {
        try {
            TextChannel textChannel = mod.getDiscordManager().getChannelById(uuid).get();

            return parseValid(mod, textChannel);
        } catch (Exception e) {
            return parseInvalid(mod, uuid);
        }
    }

    public static StringTextComponent from(IMod mod, TextChannel textChannel) {
        return parseValid(mod, textChannel);
    }

    private static StringTextComponent parseValid(IMod mod, TextChannel textChannel) {
        StringTextComponent stringText = new StringTextComponent("#" + textChannel.getName());

        stringText.setStyle(Style.EMPTY
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new StringTextComponent("Switch to #" + textChannel.getName())))
                .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                        "/" + mod.getConfigManager().getMCCommandPrefix() + " switch " + textChannel.getName()))
                .setColor(mod.getConfigManager().getMentionColor()));

        return stringText;
    }

    private static StringTextComponent parseInvalid(IMod mod, String uuid) {
        StringTextComponent stringText = new StringTextComponent("<#" + uuid + ">");

        stringText.setStyle(Style.EMPTY.setColor(mod.getConfigManager().getMentionColor()));

        return stringText;
    }
}
