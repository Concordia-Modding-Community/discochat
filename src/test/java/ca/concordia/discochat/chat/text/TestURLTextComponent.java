package ca.concordia.discochat.chat.text;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.concordia.discochat.TestMod;

public class TestURLTextComponent {
    @Test
    public void testBasic() {
        URLTextComponent urlTextComponent = new URLTextComponent(TestMod.Mocked.MOD, "http://url.com");

        assertEquals("http://url.com", urlTextComponent.getString());
    }
}
