package ca.concordia.discochat.commands;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import ca.concordia.discochat.chat.TestChatManager;
import ca.concordia.discochat.entity.TestPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;

public class TestCommand {
    public static class Mocked {
        public static CommandSource createCommandSource() throws CommandSyntaxException {
            return createCommandSource(3);
        }

        public static CommandSource createCommandSource(int permissionLevel) throws CommandSyntaxException {
            CommandSource commandSource = mock(CommandSource.class);

            when(commandSource.asPlayer()).then(i -> TestPlayerEntity.Mocked.createValid());

            when(commandSource.hasPermissionLevel(anyInt()))
                    .then(i -> permissionLevel >= i.getArgument(0, Integer.class));

            doAnswer(i -> {
                TestChatManager.Mocked.addMCMessage(i.getArgument(0, ITextComponent.class));

                return null;
            }).when(commandSource).sendFeedback(any(ITextComponent.class), anyBoolean());

            doAnswer(i -> {
                TestChatManager.Mocked.addMCMessage(i.getArgument(0, ITextComponent.class));

                return null;
            }).when(commandSource).sendErrorMessage(any(ITextComponent.class));

            return commandSource;
        }
    }
}
