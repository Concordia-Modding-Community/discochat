package ca.concordia.discochat.chat.text;

import ca.concordia.discochat.utils.IMod;
import ca.concordia.discochat.utils.IModProvider;
import net.dv8tion.jda.api.entities.Role;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class RoleTextComponent extends TextComponent implements IModProvider {
    private IMod mod;

    public RoleTextComponent(IMod mod, String uuid) {
        this.mod = mod;

        try {
            Role role = mod.getDiscordManager().getRoleById(uuid).get();

            parse(role);
        } catch (Exception e) {
            parseInvalid(uuid);
        }
    }

    public RoleTextComponent(IMod mod, Role role) {
        this.mod = mod;

        parse(role);
    }

    private void parse(Role role) {
        StringTextComponent stringText = new StringTextComponent("@" + role.getName());

        stringText.setStyle(Style.EMPTY
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new StringTextComponent("Mention Role @" + role.getName())))
                .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, role.getAsMention()))
                .setColor(Color.fromInt(role.getColor().getRGB())));

        siblings.add(stringText);
    }

    private void parseInvalid(String uuid) {
        StringTextComponent stringText = new StringTextComponent("<@&" + uuid + ">");

        stringText.setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.WHITE)));

        siblings.add(stringText);
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

}
