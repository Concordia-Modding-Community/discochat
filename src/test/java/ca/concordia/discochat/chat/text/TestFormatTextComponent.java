package ca.concordia.discochat.chat.text;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.minecraft.util.text.ITextComponent;

public class TestFormatTextComponent {
    @Test
    public void testBasic() {
        ITextComponent text = new FormatTextComponent("@h @w!").put("h", "Hello").put("w", "World").build();

        assertEquals("Hello World!", text.getString());
    }

    @Test
    public void testLong() {
        ITextComponent text = new FormatTextComponent("@hello!").put("hello", "Hello").build();

        assertEquals("Hello!", text.getString());
    }

    @Test
    public void testTouching() {
        ITextComponent text = new FormatTextComponent("@h@w!").put("h", "Hello").put("w", "World").build();

        assertEquals("HelloWorld!", text.getString());
    }

    @Test
    public void testRepeat() {
        ITextComponent text = new FormatTextComponent("@hello @world! @hello @world").put("hello", "Hello").put("world",
                "World").build();

        assertEquals("Hello World! Hello World", text.getString());
    }
}
