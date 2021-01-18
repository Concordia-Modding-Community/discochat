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

    public FormatTextComponent build() {
        Matcher matcher = PATTERN.matcher(format);

        int pointer = 0;

        while (matcher.find()) {
            int start = matcher.start();

            if (pointer < start) {
                siblings.add(new StringTextComponent(format.substring(pointer, start)));
            }

            String key = matcher.group(1);

            if (hashMap.containsKey(key)) {
                siblings.add(hashMap.get(key));
            } else {
                siblings.add(new StringTextComponent("@" + key));
            }

            pointer = matcher.end();
        }

        if (pointer < format.length()) {
            siblings.add(new StringTextComponent(format.substring(pointer)));
        }

        return this;
    }

    @Override
    public TextComponent copyRaw() {
        // TODO Auto-generated method stub
        return null;
    }
}
