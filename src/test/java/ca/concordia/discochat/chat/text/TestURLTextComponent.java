package ca.concordia.discochat.chat.text;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import net.minecraft.util.text.StringTextComponent;

public class TestURLTextComponent {
    @Test
    public void testBasic() {
        StringTextComponent urlTextComponent = URLTextComponent.from(TestMod.Mocked.MOD, "http://url.com");

        assertEquals("http://url.com", urlTextComponent.getString());
    }
}
