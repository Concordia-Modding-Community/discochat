package ca.concordia.mccord.discord;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.Test;

import ca.concordia.mccord.chat.TestChatManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

public class TestChannel {
    public static class Mocked {
        public static final String VALID_CHANNEL_UUID = "12345";
        public static final String VALID_CHANNEL_NAME = "test-channel";

        public static Guild createGuild() {
            Guild guild = mock(Guild.class);

            when(guild.getMember(any(User.class))).then(invocation -> {
                User user = invocation.getArgument(0);

                return TestUser.Mocked.createMember(user);
            });

            return guild;
        }

        public static RestAction<Object> createRestAction(Object obj) {
            RestAction<Object> restAction = mock(RestAction.class);

            doAnswer(invocation -> {
                Consumer<Object> consumer = invocation.getArgument(0);

                consumer.accept(obj);

                return null;
            }).when(restAction).queue(any(Consumer.class));

            return restAction;
        }

        public static PrivateChannel createPrivateChannel() {
            PrivateChannel privateChannel = mock(PrivateChannel.class);

            when(privateChannel.sendMessage(anyString()))
                    .then(invocation -> TestChatManager.Mocked.createMessageAction(invocation.getArgument(0)));

            return privateChannel;
        }

        public static TextChannel createValid() {
            return create(VALID_CHANNEL_NAME, VALID_CHANNEL_UUID);
        }

        public static TextChannel create(String channelName, String channelUUID) {
            TextChannel channel = mock(TextChannel.class);

            when(channel.getName()).thenReturn(channelName);

            when(channel.getId()).thenReturn(channelUUID);

            when(channel.sendMessage(anyString())).then(invocation -> {
                String message = invocation.getArgument(0, String.class);

                return TestChatManager.Mocked.createMessageAction(message);
            });

            when(channel.getGuild()).then(invocation -> createGuild());

            return channel;
        }
    }

    @Test
    public void testSendMessage() {
        TextChannel textChannel = Mocked.createValid();

        textChannel.sendMessage("Channel Message").queue();

        assertEquals("Channel Message", TestChatManager.Mocked.getLastDiscordMessage());
    }
}
