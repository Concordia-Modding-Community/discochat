package ca.concordia.discochat.chat.text;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.concordia.discochat.chat.text.FormatTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TestFormatTextComponent {
    @Test
    public void testBasic() {
        ITextComponent text = new FormatTextComponent("@h @w!", new String[] { "w", "h" },
                new ITextComponent[] { new StringTextComponent("World"), new StringTextComponent("Hello") });

        assertEquals("Hello World!", text.getString());
    }

    @Test
    public void testLong() {
        ITextComponent text = new FormatTextComponent("@hello!", new String[] { "hello" },
                new ITextComponent[] { new StringTextComponent("Hello") });

        assertEquals("Hello!", text.getString());
    }

    @Test
    public void testTouching() {
        ITextComponent text = new FormatTextComponent("@h@w!", new String[] { "w", "h" },
                new ITextComponent[] { new StringTextComponent("World"), new StringTextComponent("Hello") });

        assertEquals("HelloWorld!", text.getString());
    }

    @Test
    public void testRepeat() {
        ITextComponent text = new FormatTextComponent("@hello @world! @hello @world", new String[] { "world", "hello" },
                new ITextComponent[] { new StringTextComponent("World"), new StringTextComponent("Hello") });

        assertEquals("Hello World! Hello World", text.getString());
    }
}
