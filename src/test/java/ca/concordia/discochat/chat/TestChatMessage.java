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

public class TestChatMessage {
    @Test
    public void testBasicMessage() {
        ModUser user = TestModUser.Mocked.createValid();

        TextChannel channel = TestChannel.Mocked.createValid();

        String text = "Dual Message";

        DiscordTextComponent message = new DiscordTextComponent(TestMod.Mocked.MOD, text);

        ChatMessage chatMessage = new ChatMessage(TestMod.Mocked.MOD, user, channel, message);

        assertNotNull(user.getDiscordAsMention());

        assertEquals(new FormatTextComponent(TestMod.Mocked.MOD.getConfigManager().getDiscordTextFormat())
                .put("m", text).put("p", user.getDiscordName()).build().getString(), chatMessage.getDiscordText());

        assertEquals(
                new FormatTextComponent(TestMod.Mocked.MOD.getConfigManager().getMCTextFormat())
                        .put("c", "#" + channel.getName()).put("m", text).put("p", user.getMCName()).build().getString(),
                chatMessage.getMCText(TestPlayerEntity.Mocked.create()).getString());
    }
}