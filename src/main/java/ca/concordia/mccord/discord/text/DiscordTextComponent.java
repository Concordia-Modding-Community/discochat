package ca.concordia.mccord.discord.text;

import net.minecraft.util.text.TextComponent;

public class DiscordTextComponent extends TextComponent {
    private String text;

    public DiscordTextComponent(String text) {
        this.text = text;
    }

    @Override
    public TextComponent copyRaw() {
        // TODO Auto-generated method stub
        return null;
    }
}
