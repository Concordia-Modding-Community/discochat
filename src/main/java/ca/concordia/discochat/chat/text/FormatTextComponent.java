package ca.concordia.discochat.chat.text;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;

public class FormatTextComponent extends TextComponent {
    private static Pattern PATTERN = Pattern.compile("@([a-zA-Z_]*)");
    private HashMap<String, ITextComponent> hashMap;
    private String format;

    public FormatTextComponent(String format) {
        this.hashMap = new HashMap<String, ITextComponent>();
        this.format = format;
    }

    public FormatTextComponent put(String key, ITextComponent value) {
        hashMap.put(key, value);

        return this;
    }

    public FormatTextComponent put(String key, String value) {
        hashMap.put(key, new StringTextComponent(value));

        return this;
    }

    public StringTextComponent build() {
        StringTextComponent component = new StringTextComponent("");

        Matcher matcher = PATTERN.matcher(format);

        int pointer = 0;

        while (matcher.find()) {
            int start = matcher.start();

            if (pointer < start) {
                component.append(new StringTextComponent(format.substring(pointer, start)));
            }

            String key = matcher.group(1);

            if (hashMap.containsKey(key)) {
                component.append(hashMap.get(key));
            } else {
                component.append(new StringTextComponent("@" + key));
            }

            pointer = matcher.end();
        }

        if (pointer < format.length()) {
            component.append(new StringTextComponent(format.substring(pointer)));
        }

        return component;
    }

    @Override
    public String toString() {
        return "FormatTextComponent{}";
    }

    @Override
    public TextComponent copyRaw() {
        throw new RuntimeException("FromatTextComponent should not be used raw. Use build().");
    }

    @Override
    public String getString() {
        throw new RuntimeException("FromatTextComponent should not be used raw. Use build().");
    }
}
