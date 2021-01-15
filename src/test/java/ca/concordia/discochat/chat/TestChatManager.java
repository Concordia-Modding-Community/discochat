package ca.concordia.discochat.chat;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Stack;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.minecraft.util.text.ITextComponent;

public class TestChatManager {
    public static class Mocked {
        private static Stack<String> MC_MESSAGES = new Stack<String>();
        private static Stack<String> DISCORD_MESSAGES = new Stack<String>();

        public static void clearMessages() {
            MC_MESSAGES = new Stack<String>();
            DISCORD_MESSAGES = new Stack<String>();
        }

        private static void addMCMessage(String message) {
            MC_MESSAGES.add(message);
        }

        public static void addMCMessage(ITextComponent text) {
            addMCMessage(text.getString());
        }

        public static void addDiscordMessage(String message) {
            DISCORD_MESSAGES.add(message);
        }

        public static String getLastMCMessage() {
            return MC_MESSAGES.lastElement();
        }

        public static String getLastDiscordMessage() {
            return DISCORD_MESSAGES.lastElement();
        }

        public static MessageAction createMessageAction(String message) {
            MessageAction messageAction = mock(MessageAction.class);

            doAnswer(invocation -> {
                TestChatManager.Mocked.addDiscordMessage(message);

                return null;
            }).when(messageAction).queue();

            return messageAction;
        }

        public static Message createMessage(String text) {
            Message message = mock(Message.class);

            when(message.getContentRaw()).thenReturn(text);

            when(message.getContentDisplay()).thenReturn(text);

            when(message.getContentStripped()).thenReturn(text);

            return message;
        }
    }
}
