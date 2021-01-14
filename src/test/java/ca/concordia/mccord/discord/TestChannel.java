package ca.concordia.mccord.discord;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.concordia.mccord.chat.TestChatManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class TestChannel {
    public static class Mocked {
        public static final String VALID_CHANNEL_UUID = "12345";
        public static final String VALID_CHANNEL_NAME = "test-channel";

        public static MessageAction createMessageAction(String message) {
            MessageAction messageAction = mock(MessageAction.class);

            doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    TestChatManager.Mocked.addDiscordMessage(message);

                    return null;
                }
            }).when(messageAction).queue();

            return messageAction;
        }

        public static TextChannel create() {
            TextChannel channel = mock(TextChannel.class);

            when(channel.getName()).thenReturn(VALID_CHANNEL_NAME);

            when(channel.getId()).thenReturn(VALID_CHANNEL_UUID);

            when(channel.sendMessage(anyString())).then(invocation -> {
                String message = invocation.getArgument(0, String.class);

                return createMessageAction(message);
            });

            return channel;
        }
    }

    @Test
    public void testSendMessage() {
        TextChannel textChannel = Mocked.create();

        textChannel.sendMessage("Hello World!").queue();

        assertEquals("Hello World!", TestChatManager.Mocked.getLastDiscordMessage());
    }
}
