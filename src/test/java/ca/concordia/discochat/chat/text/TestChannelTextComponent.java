package ca.concordia.discochat.chat.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.chat.text.ChannelTextComponent;
import ca.concordia.discochat.discord.TestChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class TestChannelTextComponent {
    @Test
    public void testBasic() {
        TextChannel textChannel = TestChannel.Mocked.createValid();
        ChannelTextComponent text = new ChannelTextComponent(TestMod.Mocked.MOD, textChannel);

        assertNotNull(textChannel.getName());
        assertEquals("#" + textChannel.getName(), text.getString());
    }
}
