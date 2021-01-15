package ca.concordia.discochat.discord;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.awt.Color;

import java.util.ArrayList;

import org.junit.Test;

import ca.concordia.discochat.chat.TestChatManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class TestUser {
    public static class Mocked {
        public static final String VALID_DISCORD_UUID = "12345";
        public static final String VALID_DISCORD_NAME = "discord-dev";
        public static final String VALID_DISCORD_NUMBER = "1234";
        public static final Color VALID_COLOR = Color.BLUE;

        public static Member createMember(User user) {
            Member member = mock(Member.class);

            when(member.getRoles()).then(invocation -> {
                // TODO: add roles according to user.
                return new ArrayList<Role>();
            });

            when(member.getColor()).then(i -> VALID_COLOR);

            return member;
        }

        public static User createValid() {
            return create(VALID_DISCORD_UUID, VALID_DISCORD_NAME, VALID_DISCORD_NUMBER);
        }

        public static User create(String discordUUID, String discordName, String discordNumber) {
            User user = mock(User.class);

            when(user.getId()).thenReturn(discordUUID);

            when(user.getName()).thenReturn(discordName);

            when(user.openPrivateChannel())
                    .then(i -> TestChannel.Mocked.createRestAction(TestChannel.Mocked.createPrivateChannel()));

            when(user.getAsTag()).then(i -> discordName + "#" + discordNumber);

            when(user.getAsMention()).then(i -> "<@" + discordUUID + ">");

            return user;
        }

        public static String getValidTag() {
            return VALID_DISCORD_NAME + "#" + VALID_DISCORD_NUMBER;
        }
    }

    @Test
    public void testSendPrivateMessage() {
        User user = TestUser.Mocked.createValid();

        user.openPrivateChannel().queue(consumer -> {
            consumer.sendMessage("Private Message").queue();
        });

        assertEquals("Private Message", TestChatManager.Mocked.getLastDiscordMessage());
    }
}
