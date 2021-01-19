package ca.concordia.discochat.chat.text;

import ca.concordia.discochat.utils.IMod;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class URLTextComponent {
    public static StringTextComponent from(IMod mod, String url) {
        StringTextComponent stringText = new StringTextComponent(url);

        stringText.setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.BLUE)).setUnderlined(true)
                .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Open URL"))));

        return stringText;
    }
}
