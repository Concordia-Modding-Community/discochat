package ca.concordia.discochat.chat.text;

import ca.concordia.discochat.utils.IMod;
import ca.concordia.discochat.utils.IModProvider;
import net.dv8tion.jda.api.entities.Role;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class RoleTextComponent extends TextComponent implements IModProvider {
    private IMod mod;

    public RoleTextComponent(IMod mod, String uuid) {
        this(mod, mod.getDiscordManager().getRoleById(uuid).get());
    }

    public RoleTextComponent(IMod mod, Role role) {
        this.mod = mod;

        StringTextComponent stringText = new StringTextComponent("@" + role.getName());

        Style style = Style.EMPTY;

        style = style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new StringTextComponent("Mention Role @" + role.getName())));

        style = style.setClickEvent(
                new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, role.getAsMention()));

        style = style.setColor(Color.fromInt(role.getColor().getRGB()));

        stringText.setStyle(style);

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
