package ca.concordia.discochat.chat.text;

import ca.concordia.discochat.utils.IMod;
import ca.concordia.discochat.utils.IModProvider;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class ChannelTextComponent extends TextComponent implements IModProvider {
    private IMod mod;

    public ChannelTextComponent(IMod mod, String uuid) {
        this(mod, mod.getDiscordManager().getChannelById(uuid).get());
    }

    public ChannelTextComponent(IMod mod, TextChannel textChannel) {
        this.mod = mod;

        parse(textChannel);
    }

    private void parse(TextChannel textChannel) {
        StringTextComponent stringText = new StringTextComponent("#" + textChannel.getName());

        stringText.setStyle(Style.EMPTY
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new StringTextComponent("Switch to #" + textChannel.getName())))
                .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                        "/" + getMod().getConfigManager().getMCCommandPrefix() + " switch " + textChannel.getName()))
                .setColor(getMod().getConfigManager().getMentionColor()));

        siblings.add(stringText);
    }

    @Override
    public TextComponent copyRaw() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IMod getMod() {
        return mod;
    }
}
