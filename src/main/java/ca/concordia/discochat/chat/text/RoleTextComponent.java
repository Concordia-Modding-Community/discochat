package ca.concordia.discochat.chat.text;

import ca.concordia.discochat.utils.IMod;
import net.dv8tion.jda.api.entities.Role;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class RoleTextComponent {
    public static StringTextComponent from(IMod mod, String uuid) {
        try {
            Role role = mod.getDiscordManager().getRoleById(uuid).get();

            return parseValid(mod, role);
        } catch (Exception e) {
            return parseInvalid(mod, uuid);
        }
    }

    public static StringTextComponent from(IMod mod, Role role) {
        return parseValid(mod, role);
    }

    private static StringTextComponent parseValid(IMod mod, Role role) {
        StringTextComponent stringText = new StringTextComponent("@" + role.getName());

        stringText.setStyle(Style.EMPTY
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new StringTextComponent("Mention Role @" + role.getName())))
                .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, role.getAsMention()))
                .setColor(Color.fromInt(role.getColor().getRGB())));

        return stringText;
    }

    private static StringTextComponent parseInvalid(IMod mod, String uuid) {
        StringTextComponent stringText = new StringTextComponent("<@&" + uuid + ">");

        stringText.setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.WHITE)));

        return stringText;
    }
}