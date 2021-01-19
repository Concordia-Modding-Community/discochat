package ca.concordia.discochat.chat.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.discord.TestChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.util.text.StringTextComponent;

public class TestChannelTextComponent {
    @Test
    public void testBasic() {
        TextChannel textChannel = TestChannel.Mocked.createValid();
        StringTextComponent text = ChannelTextComponent.from(TestMod.Mocked.MOD, textChannel);

        assertNotNull(textChannel.getName());
        assertEquals("#" + textChannel.getName(), text.getString());
    }
}
