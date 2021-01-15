package ca.concordia.discochat.chat.text;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.chat.text.UserTextComponent;
import ca.concordia.discochat.discord.TestChannel;
import ca.concordia.discochat.discord.TestUser;
import ca.concordia.discochat.entity.TestPlayerEntity;

public class TestUserTextComponent {
    @Test
    public void testBasic() {
        UserTextComponent text = new UserTextComponent(TestMod.Mocked.MOD, TestUser.Mocked.createValid().getId());

        assertEquals("@" + TestPlayerEntity.Mocked.create().getName().getString(), text.getString());
    }

    @Test
    public void testWithChannel() {
        UserTextComponent text = new UserTextComponent(TestMod.Mocked.MOD, TestUser.Mocked.createValid().getId(),
                TestChannel.Mocked.createValid());

        assertEquals(TestPlayerEntity.Mocked.create().getName().getString(), text.getString());
    }
}
