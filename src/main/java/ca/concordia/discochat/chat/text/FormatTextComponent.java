package ca.concordia.discochat.chat.text;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;

public class FormatTextComponent extends TextComponent {
    private static Pattern PATTERN = Pattern.compile("@([a-zA-Z_]*)");

    public FormatTextComponent(String pattern, String[] keys, ITextComponent[] values) {
        HashMap<String, ITextComponent> hashMap = new HashMap<String, ITextComponent>();

        if (keys.length != values.length) {
            throw new RuntimeException("Key size does not match value size.");
        }

        for(int i = 0; i < keys.length; i++) {
            hashMap.put(keys[i], values[i]);
        }

        Matcher matcher = PATTERN.matcher(pattern);

        int pointer = 0;

        while (matcher.find()) {
            int start = matcher.start();

            if (pointer < start) {
                siblings.add(new StringTextComponent(pattern.substring(pointer, start)));
            }

            siblings.add(hashMap.get(matcher.group(1)));

            pointer = matcher.end();
        }

        if (pointer < pattern.length()) {
            siblings.add(new StringTextComponent(pattern.substring(pointer)));
        }
    }

    @Override
    public TextComponent copyRaw() {
        // TODO Auto-generated method stub
        return null;
    }
}
