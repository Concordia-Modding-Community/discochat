package ca.concordia.discochat.chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.chat.text.DiscordTextComponent;
import ca.concordia.discochat.chat.text.FormatTextComponent;
import ca.concordia.discochat.discord.TestChannel;
import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.entity.TestModUser;
import ca.concordia.discochat.entity.TestPlayerEntity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TestChatMessage {
    @Test
    public void testBasicMessage() {
        ModUser user = TestModUser.Mocked.createValid();

        TextChannel channel = TestChannel.Mocked.createValid();

        String text = "Dual Message";

        DiscordTextComponent message = new DiscordTextComponent(TestMod.Mocked.MOD, text);

        ChatMessage chatMessage = new ChatMessage(TestMod.Mocked.MOD, user, channel, message);

        assertNotNull(user.getDiscordAsMention());

        assertEquals(
                new FormatTextComponent(TestMod.Mocked.MOD.getConfigManager().getDiscordTextFormat(),
                        new String[] { "m", "p" },
                        new ITextComponent[] { new StringTextComponent(text),
                                new StringTextComponent(user.getDiscordName()) }).getString(),
                chatMessage.getDiscordText());

        assertEquals(
                new FormatTextComponent(TestMod.Mocked.MOD.getConfigManager().getMCTextFormat(),
                        new String[] { "c", "m", "p" },
                        new ITextComponent[] { new StringTextComponent("#" + channel.getName()),
                                new StringTextComponent(text), new StringTextComponent(user.getMCName()) })
                                        .getString(),
                chatMessage.getMCText(TestPlayerEntity.Mocked.create()).getString());
    }
}
