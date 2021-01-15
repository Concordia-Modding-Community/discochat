package ca.concordia.discochat.chat.text;

import ca.concordia.discochat.utils.IMod;
import ca.concordia.discochat.utils.IModProvider;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class URLTextComponent extends TextComponent implements IModProvider {
    private IMod mod;

    public URLTextComponent(IMod mod, String url) {
        this.mod = mod;

        StringTextComponent stringText = new StringTextComponent(url);

        Style style = Style.EMPTY;

        style = style.setColor(getMod().getConfigManager().getMentionColor());

        style = style.setUnderlined(true);

        style = style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

        style = style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Open URL")));

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
