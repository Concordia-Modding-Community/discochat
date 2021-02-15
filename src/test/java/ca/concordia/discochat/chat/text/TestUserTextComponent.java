package ca.concordia.discochat.chat.text;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.discord.TestChannel;
import ca.concordia.discochat.discord.TestUser;
import ca.concordia.discochat.entity.TestPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class TestUserTextComponent {
    @Test
    public void testBasic() {
        StringTextComponent text = UserTextComponent.from(TestMod.Mocked.MOD, TestUser.Mocked.createValid().getId());

        assertEquals("@" + TestPlayerEntity.Mocked.createValid().getName().getString(), text.getString());
    }

    @Test
    public void testWithChannel() {
        StringTextComponent text = UserTextComponent.from(TestMod.Mocked.MOD, TestUser.Mocked.createValid().getId(),
                TestChannel.Mocked.createValid());

        assertEquals(TestPlayerEntity.Mocked.createValid().getName().getString(), text.getString());
    }
}
