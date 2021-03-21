package ca.concordia.discochat.chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.chat.text.DiscordTextComponent;
import ca.concordia.discochat.chat.text.FormatTextComponent;
import ca.concordia.discochat.discord.TestChannel;
import ca.concordia.discochat.discord.TestDiscordManager;
import ca.concordia.discochat.discord.TestUser;
import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.entity.TestModUser;
import ca.concordia.discochat.entity.TestPlayerEntity;
import ca.concordia.discochat.utils.IMod;
import net.dv8tion.jda.api.entities.TextChannel;

public class TestChatMessage {
    @Test
    public void testBasicMessage() throws Exception {
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
                chatMessage.getMCText(TestPlayerEntity.Mocked.createValid()).getString());
    }

    @Test
    public void testUnverifiedMessage() throws Exception {
        ModUser user = TestModUser.Mocked.createUnverified();
        IMod mod = TestMod.Mocked.MOD;
        TextChannel channel = TestChannel.Mocked.createValid();
        ChatManager chatManager = mod.getChatManager();

        String text = "Hello World!";

        DiscordTextComponent message = new DiscordTextComponent(TestMod.Mocked.MOD, text);

        try {
            chatManager.broadcastAll(TestChatManager.Mocked.createChatMessage(user, channel, message));
        } catch(Exception e) {
        }

        // assertEquals(0, TestChatManager.Mocked.getNumberMcMessages());
        // assertEquals(0, TestChatManager.Mocked.getNumberDiscordMessages());
    }

    /**
     * TODO: Actually make this work by changing a bunch of test methods...
     */
    @Test
    public void testValidMessage() throws Exception {
        ModUser user = TestModUser.Mocked.createValid();
        IMod mod = TestMod.Mocked.MOD;
        TextChannel channel = TestChannel.Mocked.createValid();
        ChatManager chatManager = mod.getChatManager();

        String text = "Hello World!";

        DiscordTextComponent message = new DiscordTextComponent(TestMod.Mocked.MOD, text);

        try {
            chatManager.broadcastAll(TestChatManager.Mocked.createChatMessage(user, channel, message));
        } catch(Exception e) {
        }

        // assertEquals(1, TestChatManager.Mocked.getNumberMcMessages());
        // assertEquals(1, TestChatManager.Mocked.getNumberDiscordMessages());
    }
}