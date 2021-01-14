package ca.concordia.mccord.chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ca.concordia.mccord.discord.TestChannel;
import ca.concordia.mccord.entity.MCCordUser;
import ca.concordia.mccord.entity.TestMCCordUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class TestChatMessage {
    @Test
    public void testBasicMessage() {
        MCCordUser user = TestMCCordUser.Mocked.createValid();

        TextChannel channel = TestChannel.Mocked.createValid();

        String text = "Dual Message";

        ITextComponent message = new StringTextComponent(text);

        ChatMessage chatMessage = new ChatMessage(user, channel, message);

        assertNotNull(user.getDiscordAsMention());

        assertEquals(user.getDiscordAsMention() + ": " + text, chatMessage.getDiscordText());

        assertEquals(user.getMCName() + " [" + TextFormatting.BLUE + "#" + channel.getName() + TextFormatting.RESET
                + "]: " + text, chatMessage.getMCText().getString());
    }
}
