package ca.concordia.discochat.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.chat.TestChatManager;
import net.minecraft.command.CommandSource;

public class TestCommandToken {
    @Test
    public void testWithPermissions() {
        CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<CommandSource>();

        dispatcher.register(new CommandToken().register(TestMod.Mocked.MOD).getParser());

        String token = "b0t_t0k3n";

        try {
            assertEquals(1, dispatcher.execute("token " + token, TestCommand.Mocked.createCommandSource()));
            assertEquals(CommandToken.SUCCESS.getString(), TestChatManager.Mocked.getLastMCMessage());
            assertEquals(token, TestMod.Mocked.MOD.getConfigManager().getDiscordToken());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWithoutPermissions() {
        CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<CommandSource>();

        dispatcher.register(new CommandToken().register(TestMod.Mocked.MOD).getParser());

        String token = "b0t_t0k3n";

        try {
            dispatcher.execute("token " + token, TestCommand.Mocked.createCommandSource(0));

            fail("Should not be able to change token when not valid auth.");
        } catch (CommandSyntaxException e) {
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}
